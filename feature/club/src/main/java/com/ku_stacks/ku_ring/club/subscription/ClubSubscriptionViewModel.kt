package com.ku_stacks.ku_ring.club.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.club.R.string.club_subscription_subscribe_fail
import com.ku_stacks.ku_ring.club.R.string.club_subscription_unsubscribe_fail
import com.ku_stacks.ku_ring.club.subscription.contract.ClubSubscriptionSideEffect
import com.ku_stacks.ku_ring.club.subscription.contract.ClubSubscriptionUiState
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import com.ku_stacks.ku_ring.domain.club.usecase.SortClubSummariesUseCase
import com.ku_stacks.ku_ring.ui.club.ClubSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ClubSubscriptionViewModel @Inject constructor(
    private val clubRepository: ClubRepository,
    private val sortClubSummariesUseCase: SortClubSummariesUseCase,
) : ViewModel() {

    private val _subscribedIds = MutableStateFlow<Set<Int>>(emptySet())
    private val _sortOption = MutableStateFlow(ClubSortOption.END_OF_RECRUITMENT)
    val sortOption = _sortOption.asStateFlow()

    private val _uiState =
        MutableStateFlow<ClubSubscriptionUiState>(ClubSubscriptionUiState.Loading)
    val uiState: StateFlow<ClubSubscriptionUiState> = combine(
        _uiState,
        _subscribedIds,
        _sortOption,
    ) { currentState, subscribedIds, sortOption ->
        combineToUiState(currentState, subscribedIds, sortOption)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClubSubscriptionUiState.Loading
    )

    private val _sideEffect = Channel<ClubSubscriptionSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val subscriptionJobs = mutableMapOf<Int, Job>()

    init {
        fetchSubscribedClubs()
    }

    private fun combineToUiState(
        uiState: ClubSubscriptionUiState,
        subscribedIds: Set<Int>,
        sortOption: ClubSortOption,
    ): ClubSubscriptionUiState {
        val state = (uiState as? ClubSubscriptionUiState.Success) ?: return uiState
        val clubSummaries = sortClubSummariesUseCase(state.clubSummaries, sortOption.name)
            .map { it.copy(isSubscribed = subscribedIds.contains(it.id)) }
        return ClubSubscriptionUiState.Success(clubSummaries)
    }

    private fun fetchSubscribedClubs() = viewModelScope.launch {
        _uiState.update { ClubSubscriptionUiState.Loading }
        clubRepository.getSubscribedClubs()
            .onSuccess { result ->
                _subscribedIds.update { result.filter { it.isSubscribed }.map { it.id }.toSet() }
                _uiState.update { ClubSubscriptionUiState.Success(result) }
            }
            .onFailure { e ->
                Timber.e(e)
                _uiState.update { ClubSubscriptionUiState.Error(e.message) }
            }
    }

    fun updateClubSubscription(clubId: Int) {
        val isSubscribed = !_subscribedIds.value.contains(clubId)
        val isSubscribedPrevious =
            (_uiState.value as? ClubSubscriptionUiState.Success)?.clubSummaries
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
            delay(300)
            setClubSubscription(clubId, isSubscribed)
                .onSuccess {
                    _uiState.update { currentState ->
                        (currentState as? ClubSubscriptionUiState.Success)?.let { successState ->
                            val updatedState = successState.clubSummaries.map {
                                if (it.id == clubId) it.copy(isSubscribed = isSubscribed) else it
                            }
                            ClubSubscriptionUiState.Success(updatedState)
                        } ?: currentState
                    }
                }
                .onFailure { e ->
                    Timber.e(e)
                    if (isSubscribedPrevious != null) {
                        _subscribedIds.update { if (isSubscribedPrevious) it + clubId else it - clubId }
                        _sideEffect.trySend(
                            ClubSubscriptionSideEffect.ShowToast(
                                if (isSubscribed) club_subscription_subscribe_fail
                                else club_subscription_unsubscribe_fail
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
        return if (isSubscribed) clubRepository.subscribeClub(clubId)
        else clubRepository.unsubscribeClub(clubId)
    }

    fun updateSortOption(sortOption: ClubSortOption) {
        _sortOption.update { sortOption }
    }
}
