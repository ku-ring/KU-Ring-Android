package com.ku_stacks.ku_ring.main.campusmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapRecentSearch
import com.ku_stacks.ku_ring.main.campusmap.model.updateRecentSearches
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CampusMapViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CampusMapUiState.Empty)
    internal val uiState: StateFlow<CampusMapUiState> = _uiState.asStateFlow()

    init {
        fetchCampusPlaces()
    }

    private fun fetchCampusPlaces() = viewModelScope.launch {
        val places = placeRepository.getPlaces()
        _uiState.update { currentState ->
            currentState.copy(
                campusPlaces = places.toImmutableList(),
            )
        }
    }

    fun focusMapPlace(place: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = place,
                selectedSearchPlace = null,
                submittedSearchQuery = null,
                selectedCategory = null,
                focusedPlaceSource = CampusMapFocusedPlaceSource.MAP_MARKER,
            )
        }
    }

    fun clearFocusedPlace() {
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                selectedSearchPlace = null,
                focusedPlaceSource = null,
            )
        }
    }

    fun clearActiveSelection() {
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                selectedSearchPlace = null,
                submittedSearchQuery = null,
                selectedCategory = null,
                searchInput = "",
                focusedPlaceSource = null,
            )
        }
    }

    fun showLocationPermissionDialog() {
        _uiState.update { currentState ->
            currentState.copy(isLocationPermissionDialogVisible = true)
        }
    }

    fun hideLocationPermissionDialog() {
        _uiState.update { currentState ->
            currentState.copy(isLocationPermissionDialogVisible = false)
        }
    }

    internal fun updateSelectedCategory(category: CampusMapCategory) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = if (currentState.selectedCategory == category) null else category,
                focusedPlace = null,
                focusedPlaceSource = null,
            )
        }
    }

    fun prepareSearchInput() {
        _uiState.update { currentState ->
            currentState.copy(
                searchInput = currentState.activeSearchText.orEmpty(),
            )
        }
    }

    fun updateSearchInput(input: String) {
        _uiState.update { currentState ->
            currentState.copy(searchInput = input)
        }
    }

    fun clearSearchInput() {
        _uiState.update { currentState ->
            currentState.copy(searchInput = "")
        }
    }

    internal fun selectRecentSearch(recentSearch: CampusMapRecentSearch) {
        _uiState.update { currentState ->
            when (recentSearch) {
                is CampusMapRecentSearch.PlaceResult -> {
                    currentState.withSelectedSearchPlace(recentSearch.place)
                }

                is CampusMapRecentSearch.Query -> {
                    currentState.withSubmittedSearchQuery(recentSearch.query)
                }
            }
        }
    }

    internal fun deleteRecentSearch(recentSearch: CampusMapRecentSearch) {
        _uiState.update { currentState ->
            currentState.copy(
                recentSearches = currentState.recentSearches
                    .filterNot { item -> item.id == recentSearch.id }
                    .toImmutableList(),
            )
        }
    }

    fun clearRecentSearches() {
        _uiState.update { currentState ->
            currentState.copy(
                recentSearches = persistentListOf(),
            )
        }
    }

    fun selectSearchPlace(place: Place) {
        _uiState.update { currentState ->
            currentState.withSelectedSearchPlace(place)
        }
    }

    fun focusSearchResultPlace(place: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = place,
                focusedPlaceSource = CampusMapFocusedPlaceSource.SEARCH_RESULT,
            )
        }
    }

    fun submitSearch() {
        _uiState.update { currentState ->
            val submittedQuery = currentState.searchInput.trim()

            if (submittedQuery.isEmpty()) {
                currentState
            } else {
                currentState.withSubmittedSearchQuery(submittedQuery)
            }
        }
    }

    private fun CampusMapUiState.withSubmittedSearchQuery(query: String): CampusMapUiState {
        val recentSearch = CampusMapRecentSearch.Query(query)
        return copy(
            focusedPlace = null,
            selectedSearchPlace = null,
            submittedSearchQuery = query,
            selectedCategory = null,
            searchInput = query,
            focusedPlaceSource = null,
            recentSearches = updateRecentSearches(
                current = recentSearches,
                selected = recentSearch,
            ).toImmutableList(),
        )
    }

    private fun CampusMapUiState.withSelectedSearchPlace(place: Place): CampusMapUiState {
        return copy(
            focusedPlace = place,
            selectedSearchPlace = place,
            submittedSearchQuery = null,
            selectedCategory = null,
            searchInput = "",
            focusedPlaceSource = CampusMapFocusedPlaceSource.SEARCH_RESULT,
            recentSearches = updateRecentSearches(
                current = recentSearches,
                selected = CampusMapRecentSearch.PlaceResult(place),
            ).toImmutableList(),
        )
    }
}
