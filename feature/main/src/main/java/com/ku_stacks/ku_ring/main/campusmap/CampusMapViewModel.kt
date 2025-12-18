package com.ku_stacks.ku_ring.main.campusmap

import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CampusMapViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CampusMapUiState.Empty)
    val uiState: StateFlow<CampusMapUiState> = _uiState.asStateFlow()

    init {
        fetchCampusPlaces()
    }

    private fun fetchCampusPlaces(){
        val places = placeRepository.getPlaces()
        _uiState.update { currentState ->
            currentState.copy(
                campusPlaces = places.toImmutableList(),
            )
        }
    }

    fun updateFocusedPlace(place: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = place,
            )
        }
    }
}
