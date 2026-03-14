package com.ku_stacks.ku_ring.main.club.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import com.ku_stacks.ku_ring.domain.club.usecase.SortClubSummariesUseCase
import com.ku_stacks.ku_ring.main.R.string.club_subscribe_fail
import com.ku_stacks.ku_ring.main.R.string.club_unsubscribe_fail
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import com.ku_stacks.ku_ring.util.getHttpExceptionMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ClubListViewModel @Inject constructor(
    private val preferenceUtil: PreferenceUtil,
    private val clubRepository: ClubRepository,
    private val sortClubSummaries: SortClubSummariesUseCase,
) : ViewModel() {

    private val _subscribedIds = MutableStateFlow<Set<Int>>(emptySet())

    private val _clubListFilter = MutableStateFlow<ClubListFilter?>(null)
    val clubListFilter = _clubListFilter.asStateFlow()

    private val _uiState = MutableStateFlow<ClubListUiState>(ClubListUiState.Loading)
    val uiState: StateFlow<ClubListUiState> = combine(
        _uiState,
        _subscribedIds,
        clubListFilter.filterNotNull().map { it.sortOption }.distinctUntilChanged(),
    ) { currentState, subscribedIds, sortOption ->
        combineToUiState(currentState, subscribedIds, sortOption)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClubListUiState.Loading
    )

    private val _sideEffect = Channel<ClubListSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    init {
        observeInitialCategory()
        observeFilters()
    }

    private fun combineToUiState(
        uiState: ClubListUiState,
        subscribedIds: Set<Int>,
        sortOption: ClubSortOption,
    ): ClubListUiState {
        val state = (uiState as? ClubListUiState.Success) ?: return uiState
        val clubSummaries = sortClubSummaries(state.clubSummaries, sortOption.name)
            .map { clubSummary ->
                clubSummary.withSubscription(subscribedIds.contains(clubSummary.id))
            }
        return ClubListUiState.Success(clubSummaries)
    }

    private fun observeInitialCategory() {
        viewModelScope.launch {
            preferenceUtil.clubCategoryFlow
                .filter { it.isNotBlank() }
                .collect { category ->
                    val categoryEnum = ClubCategory.entries.find { entry ->
                        entry.name.equals(category, ignoreCase = true)
                    } ?: ClubCategory.ALL
                    _clubListFilter.update {
                        ClubListFilter.default().copy(selectedCategory = categoryEnum)
                    }
                }
        }
    }

    private fun observeFilters() {
        viewModelScope.launch {
            _clubListFilter
                .filterNotNull()
                .map { it.selectedCategory to it.selectedDivisions }
                .distinctUntilChanged()
                .collect { (category, divisions) ->
                    fetchClubSummary(category, divisions)
                }
        }
    }

    fun updateClubSubscription(clubId: Int) {
        val clubSummary = (_uiState.value as? ClubListUiState.Success)?.clubSummaries
            ?.find { it.id == clubId } ?: return
        val isSubscribed = !_subscribedIds.value.contains(clubId)
        val isSubscribedPrevious = clubSummary.isSubscribed

        _subscribedIds.update { if (isSubscribed) it + clubId else it - clubId }
        handleSubscription(clubId, isSubscribed, isSubscribedPrevious)
    }

    private fun handleSubscription(
        clubId: Int,
        isSubscribed: Boolean,
        isSubscribedPrevious: Boolean?,
    ) {
        subscriptionJobs[clubId]?.cancel()
        val job = viewModelScope.launch {
            delay(300)
            setClubSubscription(clubId, isSubscribed)
                .onSuccess {
                    _uiState.update { currentState ->
                        (currentState as? ClubListUiState.Success)?.let { successState ->
                            val updatedState = successState.clubSummaries.map {
                                if (it.id == clubId) it.withSubscription(isSubscribed)
                                else it
                            }
                            ClubListUiState.Success(updatedState)
                        } ?: currentState
                    }
                }
                .onFailure { e ->
                    Timber.e("Failed to update club subscription: $e")
                    if (isSubscribedPrevious != null) {
                        _subscribedIds.update { if (isSubscribedPrevious) it + clubId else it - clubId }
                        _sideEffect.trySend(
                            ClubListSideEffect.ShowToast(
                                if (isSubscribed) club_subscribe_fail
                                else club_unsubscribe_fail
                            )
                        )
                    }
                }
        }
        subscriptionJobs[clubId] = job
        job.invokeOnCompletion {
            if (subscriptionJobs[clubId] === job) {
                subscriptionJobs.remove(clubId)
            }
        }
    }

    private suspend fun setClubSubscription(clubId: Int, isSubscribed: Boolean): Result<Int> {
        return try {
            if (isSubscribed) clubRepository.subscribeClub(clubId)
            else clubRepository.unsubscribeClub(clubId)
        } catch (e: Exception) {
            val exception = e.getHttpExceptionMessage()?.let { Exception(it.message) } ?: e
            Result.failure(exception)
        }
    }

    private suspend fun fetchClubSummary(
        category: ClubCategory,
        divisions: Set<ClubDivision>,
    ) {
        // API 호출 전 상태를 초기화
        _uiState.update { ClubListUiState.Loading }

        // API 호출 후 UI 상태 및 구독 상태 업데이트
        clubRepository.getClubs(category, divisions)
            .onSuccess { clubSummaries ->
                _subscribedIds.update {
                    clubSummaries.asSequence()
                        .filter { it.isSubscribed }
                        .map { it.id }
                        .toSet()
                }
                _uiState.update { ClubListUiState.Success(clubSummaries) }
            }
            .onFailure { e ->
                Timber.e(e)
                _uiState.update { ClubListUiState.Error(e.message.toString()) }
            }
    }

    fun isUserLoggedIn(): Boolean = preferenceUtil.accessToken.isNotEmpty()

    fun updateSelectedCategory(category: ClubCategory) {
        _clubListFilter.update { it?.copy(selectedCategory = category) }
    }

    fun updateSelectedDivisions(divisions: Set<ClubDivision>) {
        _clubListFilter.update { it?.copy(selectedDivisions = divisions) }
    }

    fun resetSelectedDivisions() {
        _clubListFilter.update { it?.copy(selectedDivisions = setOf()) }
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _clubListFilter.update { it?.copy(sortOption = sortOption) }
    }
}

/**
 * ClubSummary의 구독 여부를 업데이트하는 확장 함수입니다.
 * 기존 구독 여부와 새로운 구독 여부를 비교하여 구독 수를 증가/감소/유지합니다.
 */
private fun ClubSummary.withSubscription(isSubscribed: Boolean): ClubSummary {
    val countDelta = when {
        isSubscribed && !this.isSubscribed -> 1
        !isSubscribed && this.isSubscribed -> -1
        else -> 0
    }
    return this.copy(
        isSubscribed = isSubscribed,
        subscribeCount = (this.subscribeCount + countDelta).coerceAtLeast(0),
    )
}
