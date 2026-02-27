package com.ku_stacks.ku_ring.club.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import com.ku_stacks.ku_ring.domain.isRecruitmentCompleted
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ClubSubscriptionViewModel @Inject constructor(
    private val clubRepository: ClubRepository
) : ViewModel() {
    private val _sortOption = MutableStateFlow(ClubSortOption.END_OF_RECRUITMENT)
    val sortOption = _sortOption.asStateFlow()

    private val _clubSummaries = MutableStateFlow<List<ClubSummary>>(emptyList())
    val clubSummaries: StateFlow<List<ClubSummary>> =
        combine(_clubSummaries, _sortOption) { summaries, sortOption ->
            when (sortOption) {
                ClubSortOption.END_OF_RECRUITMENT -> summaries.sortedWith(
                    compareBy<ClubSummary> { it.isRecruitmentCompleted() }
                        .thenBy { it.recruitmentEnd }
                )
                ClubSortOption.ALPHABETIC -> summaries.sortedWith(
                    compareBy<ClubSummary> { it.isRecruitmentCompleted() }
                        .thenBy { it.name }
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    init {
        fetchSubscribedClubs()
    }

    private fun fetchSubscribedClubs() = viewModelScope.launch {
        clubRepository.getSubscribedClubs()
            .onSuccess { result ->
                _clubSummaries.update { result }
            }
            .onFailure(Timber::e)
    }

    /**
     * 사용자의 동아리 구독 상태를 업데이트합니다.
     * @param clubSummary 새로운 구독상태를 포함하는 동아리 정보
     */
    fun updateClubSubscription(clubSummary: ClubSummary) {
        val clubId = clubSummary.id
        val newState = clubSummary.isSubscribed
        _clubSummaries.update {
            it.map { item ->
                if (item.id == clubId) item.copy(isSubscribed = newState) else item
            }
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

    private suspend fun setClubSubscription(clubId: Int, isSubscribed: Boolean) {
        if (isSubscribed) {
            clubRepository.subscribeClub(clubId)
        } else {
            clubRepository.unsubscribeClub(clubId)
        }
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _sortOption.update { sortOption }
    }
}
