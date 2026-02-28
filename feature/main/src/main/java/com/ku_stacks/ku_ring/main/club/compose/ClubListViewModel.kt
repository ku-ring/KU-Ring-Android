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
    private val sortClubSummariesUseCase: SortClubSummariesUseCase,
) : ViewModel() {

    private val _subscribedIds = MutableStateFlow<Set<Int>>(emptySet())

    private val _chatListFilter = MutableStateFlow(
        ClubListFilter(
            selectedCategory = getInitialCategory(),
            selectedDivisions = setOf(),
            sortOption = ClubSortOption.END_OF_RECRUITMENT
        )
    )
    val clubListFilter: StateFlow<ClubListFilter> = _chatListFilter.asStateFlow()

    private val _uiState = MutableStateFlow<ClubListUiState>(ClubListUiState.Loading)
    val uiState: StateFlow<ClubListUiState> = combine(
        _uiState,
        _subscribedIds,
        clubListFilter.map { it.sortOption }.distinctUntilChanged(),
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
        observeFilters()
    }

    private fun combineToUiState(
        uiState: ClubListUiState,
        subscribedIds: Set<Int>,
        sortOption: ClubSortOption,
    ): ClubListUiState {
        val state = (uiState as? ClubListUiState.Success) ?: return uiState
        val clubSummaries = sortClubSummariesUseCase(state.clubSummaries, sortOption.name)
            .map { it.copy(isSubscribed = subscribedIds.contains(it.id)) }
        return ClubListUiState.Success(clubSummaries)
    }

    private fun observeFilters() {
        viewModelScope.launch {
            _chatListFilter
                .map { it.selectedCategory to it.selectedDivisions }
                .distinctUntilChanged()
                .collect { (category, divisions) ->
                    if(category == null) return@collect
                    fetchClubSummary(category, divisions)
                }
        }
    }

    fun updateClubSubscription(clubSummary: ClubSummary) {
        val clubId = clubSummary.id
        val isSubscribed = clubSummary.isSubscribed
        val isSubscribedPrevious =
            (_uiState.value as? ClubListUiState.Success)?.clubSummaries
                ?.find { it.id == clubId }?.isSubscribed

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
            try {
                delay(300)
                setClubSubscription(clubId, isSubscribed)
                    .onSuccess {
                        _uiState.update { currentState ->
                            (currentState as? ClubListUiState.Success)?.let { successState ->
                                val updatedState = successState.clubSummaries.map {
                                    if (it.id == clubId) it.copy(isSubscribed = isSubscribed) else it
                                }
                                ClubListUiState.Success(updatedState)
                            } ?: currentState
                        }
                    }
                    .onFailure { e ->
                        Timber.e(e)
                        if (isSubscribedPrevious != null) {
                            _subscribedIds.update { if (isSubscribedPrevious) it + clubId else it - clubId }
                            _sideEffect.send(
                                ClubListSideEffect.ShowToast(
                                    if (isSubscribed) club_subscribe_fail
                                    else club_unsubscribe_fail
                                )
                            )
                        }
                    }
            } finally {
                if (subscriptionJobs[clubId] === this) {
                    subscriptionJobs.remove(clubId)
                }
            }
        }
        subscriptionJobs[clubId] = job
    }

    private suspend fun setClubSubscription(clubId: Int, isSubscribed: Boolean): Result<Int> {
        return if (isSubscribed) clubRepository.subscribeClub(clubId)
        else clubRepository.unsubscribeClub(clubId)
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
                    clubSummaries.filter { it.isSubscribed }.map { it.id }.toSet()
                }
                _uiState.update { ClubListUiState.Success(clubSummaries) }
            }
            .onFailure { e ->
                Timber.e(e)
                _uiState.update { ClubListUiState.Error(e.message.toString()) }
            }
    }

    fun getInitialCategory(): ClubCategory? {
        val saved = preferenceUtil.clubInitialCategory
        if (saved.isEmpty()) return null
        return ClubCategory.entries.find { it.name.lowercase() == saved }
    }

    fun isUserLoggedIn(): Boolean = preferenceUtil.accessToken.isNotEmpty()


    fun updateSelectedCategory(category: ClubCategory?) {
        _chatListFilter.update { it.copy(selectedCategory = category) }
    }

    fun updateSelectedDivisions(divisions: Set<ClubDivision>) {
        _chatListFilter.update { it.copy(selectedDivisions = divisions) }
    }

    fun resetSelectedDivisions() {
        _chatListFilter.update { it.copy(selectedDivisions = setOf()) }
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _chatListFilter.update { it.copy(sortOption = sortOption) }
    }
}
