package com.ku_stacks.ku_ring.main.club.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
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
class ClubListViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ClubListUiState.empty())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _rawClubsFlow: Flow<PagingData<Club>> = _uiState.flatMapLatest { uiState ->
        getClubs(
            category = uiState.selectedCategory,
            divisions = uiState.selectedDivisions,
            sortOption = uiState.sortOption,
        )
    }.cachedIn(viewModelScope)
    private val _subscriptionOverride = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    val clubsFlow = combine(_rawClubsFlow, _subscriptionOverride) { pagingData, overrides ->
        pagingData.map { club ->
            val isSubscribed = overrides[club.id] ?: club.isSubscribed
            club.copy(isSubscribed = isSubscribed)
        }
    }

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    /**
     * 사용자의 club 구독 상태를 업데이트합니다.
     * @param club 새로운 구독상태를 포함하는 동아리 정보
     */
    fun updateClubSubscription(club: Club) {
        val clubId = club.id
        val newState = _subscriptionOverride.value[clubId]?.not() ?: club.isSubscribed

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

    fun updateBottomSheetVisibility() {
        _uiState.update { it.copy(isDivisionBottomSheetVisible = !it.isDivisionBottomSheetVisible) }
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
    }

    private fun getClubs(
        category: ClubCategory,
        divisions: Set<ClubDivision>,
        sortOption: ClubSortOption,
    ): Flow<PagingData<Club>> {
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

    companion object {
        private const val TAG = "ClubListViewModel"
    }
}
