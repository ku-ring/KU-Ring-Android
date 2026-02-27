package com.ku_stacks.ku_ring.main.club.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import com.ku_stacks.ku_ring.domain.isRecruitmentCompleted
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ClubListViewModel @Inject constructor(
    private val preferenceUtil: PreferenceUtil,
    private val clubRepository: ClubRepository
) : ViewModel() {
    private val _chatListFilter = MutableStateFlow(ClubListFilter(
        selectedCategory = getInitialCategory(),
        selectedDivisions = setOf(),
        sortOption = ClubSortOption.END_OF_RECRUITMENT
    ))
    val chatListFilter: StateFlow<ClubListFilter> = _chatListFilter.asStateFlow()
    private val _serverParams = _chatListFilter
        .map { it.selectedCategory to it.selectedDivisions }
        .distinctUntilChanged()

    private val _sortOption = _chatListFilter
        .map { it.sortOption }
        .distinctUntilChanged()

    private val _uiState = MutableStateFlow<ClubListUiState>(ClubListUiState.Loading)
    val uiState: StateFlow<ClubListUiState> =
        combine(_uiState, _sortOption) { currentState, sortOption ->
            val state = (currentState as? ClubListUiState.Success) ?: return@combine currentState
            val clubSummaries = when (sortOption) {
                ClubSortOption.END_OF_RECRUITMENT -> state.clubSummaries.sortedWith(
                    compareBy<ClubSummary> { it.isRecruitmentCompleted() }
                        .thenBy { it.recruitmentEnd }
                )

                ClubSortOption.ALPHABETIC -> state.clubSummaries.sortedWith(
                    compareBy<ClubSummary> { it.isRecruitmentCompleted() }
                        .thenBy { it.name }
                )
            }
            ClubListUiState.Success(clubSummaries)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ClubListUiState.Loading
        )

    init {
        viewModelScope.launch {
            _serverParams.collect { (category, divisions) ->
                fetchClubSummary(
                    _chatListFilter.value.selectedCategory,
                    _chatListFilter.value.selectedDivisions
                )
            }
        }
    }

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    fun updateClubSubscription(clubSummary: ClubSummary) {
        val clubId = clubSummary.id
        val newState = clubSummary.isSubscribed
        _uiState.update {
            val previous = (it as? ClubListUiState.Success)?.clubSummaries ?: emptyList()
            ClubListUiState.Success(previous.map { item ->
                if (item.id == clubId) item.copy(isSubscribed = newState) else item
            })
        }

        viewModelScope.launch {
            handleSubscription(clubId, newState)
        }
    }

    private fun handleSubscription(clubId: Int, newState: Boolean) {
        subscriptionJobs[clubId]?.cancel()

        val job = viewModelScope.launch {
            try {
                delay(300)
                setClubSubscription(clubId, newState)
            } finally {
                if (subscriptionJobs[clubId] === this) {
                    subscriptionJobs.remove(clubId)
                }
            }
        }

        subscriptionJobs[clubId] = job
    }

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

    private suspend fun fetchClubSummary(
        category: ClubCategory?,
        divisions: Set<ClubDivision>,
    ) {
        if(category == null) {
            _uiState.update { ClubListUiState.Error("category is null") }
            return
        }

        _uiState.update { ClubListUiState.Loading }
        clubRepository.getClubs(category, divisions)
            .onSuccess { clubSummaries ->
                _uiState.update {
                    ClubListUiState.Success(clubSummaries)
                }
            }
            .onFailure { e ->
                Timber.e(e)
                _uiState.update { ClubListUiState.Error(e.message.toString()) }
            }
    }

    private suspend fun setClubSubscription(clubId: Int, isSubscribed: Boolean) {
        if (isSubscribed) {
            clubRepository.subscribeClub(clubId)
        } else {
            clubRepository.unsubscribeClub(clubId)
        }
    }

    fun getInitialCategory(): ClubCategory? {
        val saved = preferenceUtil.clubInitialCategory
        if (saved.isEmpty()) return null
        return ClubCategory.entries.find { it.name.lowercase() == saved }
    }

    fun isUserLoggedIn(): Boolean = preferenceUtil.accessToken.isNotEmpty()
}
