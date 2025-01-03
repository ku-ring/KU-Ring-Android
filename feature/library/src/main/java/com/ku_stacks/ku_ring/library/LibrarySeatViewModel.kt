package com.ku_stacks.ku_ring.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.library.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibrarySeatViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibrarySeatUiState.Empty)
    val uiState = _uiState.asStateFlow()

    fun getLibrarySeatStatus() = viewModelScope.launch {
        updateIsLoading( true)

        libraryRepository.getRemainingSeats()
            .onSuccess { rooms ->
                if (rooms.isNotEmpty()) {
                    updateLoadState(SeatLoadState.Success(rooms))
                }
            }.onFailure {
                updateLoadState(SeatLoadState.Error)
            }
    }

    private fun updateIsLoading(isLoading: Boolean) = _uiState.update { currentState ->
        currentState.copy(
            isLoading = isLoading
        )
    }

    private fun updateLoadState(loadState: SeatLoadState) {
        _uiState.update { currentState ->
            currentState.copy(
                loadState = loadState,
                isLoading = false
            )
        }
    }
}