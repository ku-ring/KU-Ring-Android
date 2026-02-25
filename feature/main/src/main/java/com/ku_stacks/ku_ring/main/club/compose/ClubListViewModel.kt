package com.ku_stacks.ku_ring.main.club.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ClubListViewModel @Inject constructor(
    private val preferenceUtil: PreferenceUtil,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClubListUiState.empty())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _rawClubSummaryFlow: Flow<PagingData<ClubSummary>> = _uiState.flatMapLatest { uiState ->
        getClubSummaries(
            category = uiState.selectedCategory,
            divisions = uiState.selectedDivisions,
            sortOption = uiState.sortOption,
        )
    }.cachedIn(viewModelScope)
    private val _subscriptionOverride = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    val clubsFlow = combine(_rawClubSummaryFlow, _subscriptionOverride) { pagingData, overrides ->
        pagingData.map { clubSummary ->
            val isSubscribed = overrides[clubSummary.id] ?: clubSummary.isSubscribed
            clubSummary.copy(isSubscribed = isSubscribed)
        }
    }

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    /**
     * 사용자의 동아리 구독 상태를 업데이트합니다.
     * @param clubSummary 새로운 구독상태를 포함하는 동아리 정보
     */
    fun updateClubSubscription(clubSummary: ClubSummary) {
        val clubId = clubSummary.id
        val newState = _subscriptionOverride.value[clubId]?.not() ?: clubSummary.isSubscribed

        _subscriptionOverride.update { it + (clubId to newState) }
        viewModelScope.launch {
            Timber
                .tag(TAG)
                .d("Clicking Subscribe -> clubId: $clubId, isSubscribed: $newState")
            handleSubscription(clubId, newState)
        }
    }

    private fun handleSubscription(clubId: Int, newState: Boolean) {
        subscriptionJobs[clubId]?.cancel()

        val job = viewModelScope.launch {
            try {
                delay(300)
                // TODO: 실제 API 호출

                Timber
                    .tag(TAG)
                    .d("Sending API -> clubId: $clubId, isSubscribed: $newState")
            } finally {
                if (subscriptionJobs[clubId] === this) {
                    subscriptionJobs.remove(clubId)
                }
            }
        }

        subscriptionJobs[clubId] = job
    }

    fun updateSelectedCategory(category: ClubCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSelectedDivisions(divisions: Set<ClubDivision>) {
        _uiState.update {
            it.copy(selectedDivisions = divisions)
        }
    }

    fun resetSelectedDivisions() {
        _uiState.update { it.copy(selectedDivisions = setOf()) }
    }

    fun updateBottomSheetVisibility() {
        _uiState.update { it.copy(isDivisionBottomSheetVisible = !it.isDivisionBottomSheetVisible) }
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
    }

    private fun getClubSummaries(
        category: ClubCategory,
        divisions: Set<ClubDivision>,
        sortOption: ClubSortOption,
    ): Flow<PagingData<ClubSummary>> {
        // TODO: 실제 API 호출
        return flowOf(
            PagingData.empty(
                LoadStates(
                    refresh = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false)
                )
            )
        )
    }

    fun isUserLoggedIn(): Boolean = preferenceUtil.accessToken.isNotEmpty()

    companion object {
        private const val TAG = "ClubListViewModel"
    }
}
