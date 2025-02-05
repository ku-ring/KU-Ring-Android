package com.ku_stacks.ku_ring.library

import com.ku_stacks.ku_ring.domain.LibraryRoom

data class LibrarySeatUiState(
    val isLoading: Boolean,
    val loadState: SeatLoadState,
){
    companion object {
        val Empty = LibrarySeatUiState(
            isLoading = false,
            loadState = SeatLoadState.InitialLoading,
        )
    }
}

sealed interface SeatLoadState {
    data object InitialLoading : SeatLoadState
    data object Error : SeatLoadState
    data class Success(val rooms:  List<LibraryRoom>) : SeatLoadState
}