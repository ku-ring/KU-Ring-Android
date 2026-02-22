package com.ku_stacks.ku_ring.club.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ku_stacks.ku_ring.domain.Club
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
import javax.inject.Inject

@HiltViewModel
class ClubSubscriptionViewModel @Inject constructor() : ViewModel() {
    private val _sortOption = MutableStateFlow(ClubSortOption.END_OF_RECRUITMENT)
    val sortOption = _sortOption.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _rawSubscribedClubsFlow: Flow<PagingData<Club>> =
        _sortOption.flatMapLatest { sortOption ->
            getSubscribedClubs(sortOption)
        }.cachedIn(viewModelScope)
    private val _subscriptionOverride = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    val subscribedClubsFlow =
        combine(_rawSubscribedClubsFlow, _subscriptionOverride) { pagingData, overrides ->
            pagingData.map { club ->
                val isSubscribed = overrides[club.id] ?: club.isSubscribed
                club.copy(isSubscribed = isSubscribed)
            }
        }

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    private fun getSubscribedClubs(
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

    /**
     * 사용자의 club 구독 상태를 업데이트합니다.
     * @param club 새로운 구독상태를 포함하는 동아리 정보
     */
    fun updateClubSubscription(club: Club) {
        val clubId = club.id
        val newState = _subscriptionOverride.value[clubId]?.not() ?: club.isSubscribed

        _subscriptionOverride.update { it + (clubId to newState) }
        handleSubscription(clubId, newState)
    }

    private fun handleSubscription(clubId: Int, newState: Boolean) {
        subscriptionJobs[clubId]?.cancel()

        val job = viewModelScope.launch {
            try {
                delay(300)
                // TODO: 실제 API 호출
            } finally {
                if (subscriptionJobs[clubId] === this) {
                    subscriptionJobs.remove(clubId)
                }
            }
        }

        subscriptionJobs[clubId] = job
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _sortOption.update { sortOption }
    }
}
