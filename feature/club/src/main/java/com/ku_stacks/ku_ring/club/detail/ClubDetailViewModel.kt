package com.ku_stacks.ku_ring.club.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.club.R
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val clubRepository: ClubRepository,
) : ViewModel() {

    var clubId by mutableIntStateOf(savedStateHandle.get<Int>(ClubDetailActivity.CLUB_ID_KEY) ?: -1)
        private set

    private val _clubUiState = MutableStateFlow<ClubDetailUiState>(ClubDetailUiState.Loading)
    val clubUiState = _clubUiState.asStateFlow()

    private val _toastByResource = MutableSharedFlow<Int>()
    val toastByResource = _toastByResource.asSharedFlow()

    private var subscriptionJob: Job? = null

    init {
        loadClub()
    }

    fun loadClub() {
        viewModelScope.launch {
            clubRepository.getClubDetail(clubId).onSuccess {
                _clubUiState.value = ClubDetailUiState.Success(it)
            }.onFailure {
                _clubUiState.value = ClubDetailUiState.Error(it.message)
            }
        }
    }

    fun updateSubscription(newState: Boolean) {
        subscriptionJob?.cancel()
        subscriptionJob = viewModelScope.launch {
            try {
                if (newState) {
                    clubRepository.subscribeClub(clubId).onSuccess {
                        updateUiStateAfterSubscription(it, true)
                    }.onFailure {
                        _toastByResource.emit(R.string.club_detail_subscribe_fail)
                    }
                } else {
                    clubRepository.unsubscribeClub(clubId).onSuccess {
                        updateUiStateAfterSubscription(it, false)
                    }.onFailure { _toastByResource.emit(R.string.club_detail_unsubscribe_fail) }
                }
            } finally {
                if (subscriptionJob === this) {
                    subscriptionJob = null
                }
            }
        }
    }

    private fun updateUiStateAfterSubscription(newSubscriptionCount: Int, newState: Boolean) {
        val uiState = _clubUiState.value
        if (uiState is ClubDetailUiState.Success) {
            _clubUiState.value = ClubDetailUiState.Success(
                uiState.club.copy(
                    subscribeCount = newSubscriptionCount,
                    isSubscribed = newState,
                )
            )
        }
    }
}