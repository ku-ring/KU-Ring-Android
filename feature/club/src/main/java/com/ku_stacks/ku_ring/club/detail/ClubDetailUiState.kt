package com.ku_stacks.ku_ring.club.detail

import com.ku_stacks.ku_ring.domain.Club

sealed interface ClubDetailUiState {
    data object Loading : ClubDetailUiState
    data class Success(val club: Club) : ClubDetailUiState
    data class Error(val message: String?) : ClubDetailUiState
}
