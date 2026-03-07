package com.ku_stacks.ku_ring.club.subscription.contract

import com.ku_stacks.ku_ring.domain.ClubSummary

sealed interface ClubSubscriptionUiState {
    data object Loading : ClubSubscriptionUiState
    data class Success(val clubSummaries: List<ClubSummary>) : ClubSubscriptionUiState
    data class Error(val message: String?) : ClubSubscriptionUiState
}
