package com.ku_stacks.ku_ring.main.campusmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapRecentSearch
import com.ku_stacks.ku_ring.main.campusmap.model.groupByCampusBuilding
import com.ku_stacks.ku_ring.main.campusmap.model.updateRecentSearches
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.ku_stacks.ku_ring.main.campusmap.type.toCampusMapCategoryItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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

    private val placeDetailCache = mutableMapOf<String, Place>()
    private var initialLoadJob: Job? = null
    private var categoryJob: Job? = null
    private var liveSearchJob: Job? = null
    private var submittedSearchJob: Job? = null
    private var placeDetailJob: Job? = null

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        initialLoadJob?.cancel()
        initialLoadJob = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isInitialLoading = true,
                    isInitialLoadFailed = false,
                )
            }

            val (buildingResult, categoryResult) = coroutineScope {
                val buildings = async { placeRepository.getPlaceBuildings() }
                val categories = async { placeRepository.getPlaceCategories() }
                buildings.await() to categories.await()
            }

            _uiState.update { currentState ->
                currentState.copy(
                    campusPlaces = buildingResult
                        .getOrNull()
                        ?.toImmutableList()
                        ?: currentState.campusPlaces,
                    categories = categoryResult
                        .getOrNull()
                        ?.toCampusMapCategoryItems()
                        ?.toImmutableList()
                        ?: currentState.categories,
                    isInitialLoading = false,
                    isInitialLoadFailed = buildingResult.isFailure || categoryResult.isFailure,
                )
            }
        }
    }

    fun focusMapPlace(place: Place) {
        categoryJob?.cancel()
        liveSearchJob?.cancel()
        submittedSearchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                selectedSearchPlace = null,
                submittedSearchQuery = null,
                selectedCategory = null,
                categoryPlaces = persistentListOf(),
                searchPlaces = persistentListOf(),
                searchResultQuery = null,
                liveSearchPlaces = persistentListOf(),
                liveSearchResultQuery = null,
                searchInput = "",
                isCategoryLoading = false,
                isSubmittedSearchLoading = false,
                isLiveSearchLoading = false,
                failedCategory = null,
                failedSubmittedSearchQuery = null,
                failedLiveSearchQuery = null,
            )
        }
        fetchPlaceDetail(place, CampusMapFocusedPlaceSource.MAP_MARKER)
    }

    fun clearFocusedPlace() {
        placeDetailJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                pendingFocusedPlace = null,
                selectedSearchPlace = null,
                focusedPlaceSource = null,
                failedPlaceDetail = null,
            )
        }
    }

    fun clearActiveSelection() {
        categoryJob?.cancel()
        liveSearchJob?.cancel()
        submittedSearchJob?.cancel()
        placeDetailJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                pendingFocusedPlace = null,
                selectedSearchPlace = null,
                submittedSearchQuery = null,
                selectedCategory = null,
                categoryPlaces = persistentListOf(),
                searchPlaces = persistentListOf(),
                searchResultQuery = null,
                liveSearchPlaces = persistentListOf(),
                liveSearchResultQuery = null,
                searchInput = "",
                isCategoryLoading = false,
                isSubmittedSearchLoading = false,
                isLiveSearchLoading = false,
                focusedPlaceSource = null,
                failedCategory = null,
                failedSubmittedSearchQuery = null,
                failedLiveSearchQuery = null,
                failedPlaceDetail = null,
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
        val shouldSelect = _uiState.value.selectedCategory != category
        categoryJob?.cancel()
        placeDetailJob?.cancel()

        if (!shouldSelect) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCategory = null,
                    categoryPlaces = persistentListOf(),
                    focusedPlace = null,
                    pendingFocusedPlace = null,
                    focusedPlaceSource = null,
                    isCategoryLoading = false,
                    failedCategory = null,
                    failedPlaceDetail = null,
                )
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = category,
                categoryPlaces = persistentListOf(),
                focusedPlace = null,
                pendingFocusedPlace = null,
                focusedPlaceSource = null,
                isCategoryLoading = true,
                failedCategory = null,
                failedPlaceDetail = null,
            )
        }
        fetchCategoryPlaces(category)
    }

    private fun fetchCategoryPlaces(category: CampusMapCategory) {
        categoryJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                isCategoryLoading = true,
                failedCategory = null,
            )
        }
        categoryJob = viewModelScope.launch {
            val result = placeRepository.getPlaceCampusPlaces(arrayOf(category.apiName))
            _uiState.update { currentState ->
                if (currentState.selectedCategory != category) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { facilities ->
                        currentState.copy(
                            categoryPlaces = facilities
                                .groupByCampusBuilding()
                                .toImmutableList(),
                            isCategoryLoading = false,
                            failedCategory = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            categoryPlaces = persistentListOf(),
                            isCategoryLoading = false,
                            failedCategory = category,
                        )
                    },
                )
            }
        }
    }

    fun prepareSearchInput() {
        val preparedInput = _uiState.value.activeSearchText.orEmpty()
        _uiState.update { currentState ->
            currentState.copy(searchInput = preparedInput)
        }

        if (preparedInput.isBlank()) {
            clearSearchInput()
            return
        }

        if (
            _uiState.value.liveSearchResultQuery != preparedInput.trim()
        ) {
            searchLiveBuildings(
                query = preparedInput,
                debounce = true,
            )
        }
    }

    fun updateSearchInput(input: String) {
        _uiState.update { currentState ->
            currentState.copy(searchInput = input)
        }
        searchLiveBuildings(
            query = input,
            debounce = true,
        )
    }

    fun clearSearchInput() {
        liveSearchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                searchInput = "",
                liveSearchPlaces = persistentListOf(),
                liveSearchResultQuery = null,
                isLiveSearchLoading = false,
                failedLiveSearchQuery = null,
            )
        }
    }

    internal fun selectRecentSearch(recentSearch: CampusMapRecentSearch) {
        cancelRequestsForCommittedSearch()
        when (recentSearch) {
            is CampusMapRecentSearch.PlaceResult -> {
                _uiState.update { currentState ->
                    currentState.withSelectedSearchPlace(recentSearch.place)
                }
                fetchPlaceDetail(
                    place = recentSearch.place,
                    source = CampusMapFocusedPlaceSource.SEARCH_RESULT,
                )
            }

            is CampusMapRecentSearch.Query -> {
                _uiState.update { currentState ->
                    currentState.withSubmittedSearchQuery(recentSearch.query)
                }
                searchSubmittedBuildings(recentSearch.query)
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
            currentState.copy(recentSearches = persistentListOf())
        }
    }

    fun selectSearchPlace(place: Place) {
        cancelRequestsForCommittedSearch()
        _uiState.update { currentState ->
            currentState.withSelectedSearchPlace(place)
        }
        fetchPlaceDetail(place, CampusMapFocusedPlaceSource.SEARCH_RESULT)
    }

    fun focusSearchResultPlace(place: Place) {
        fetchPlaceDetail(place, CampusMapFocusedPlaceSource.SEARCH_RESULT)
    }

    fun submitSearch() {
        val submittedQuery = _uiState.value.searchInput.trim()
        if (submittedQuery.isEmpty()) return

        cancelRequestsForCommittedSearch()
        _uiState.update { currentState ->
            currentState.withSubmittedSearchQuery(submittedQuery)
        }
        searchSubmittedBuildings(submittedQuery)
    }

    private fun cancelRequestsForCommittedSearch() {
        categoryJob?.cancel()
        liveSearchJob?.cancel()
        submittedSearchJob?.cancel()
        placeDetailJob?.cancel()
    }

    private fun searchLiveBuildings(
        query: String,
        debounce: Boolean,
    ) {
        val normalizedQuery = query.trim()
        liveSearchJob?.cancel()

        if (normalizedQuery.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    liveSearchPlaces = persistentListOf(),
                    liveSearchResultQuery = null,
                    isLiveSearchLoading = false,
                    failedLiveSearchQuery = null,
                )
            }
            return
        }

        _uiState.update { currentState ->
            val alreadyHasResult = currentState.liveSearchResultQuery == normalizedQuery
            currentState.copy(
                liveSearchPlaces = if (alreadyHasResult) {
                    currentState.liveSearchPlaces
                } else {
                    persistentListOf()
                },
                liveSearchResultQuery = currentState.liveSearchResultQuery
                    .takeIf { alreadyHasResult },
                isLiveSearchLoading = true,
                failedLiveSearchQuery = null,
            )
        }

        liveSearchJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MILLIS)

            val result = placeRepository.searchPlaceBuildings(normalizedQuery)
            _uiState.update { currentState ->
                if (currentState.searchInput.trim() != normalizedQuery) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { places ->
                        currentState.copy(
                            liveSearchPlaces = places.toImmutableList(),
                            liveSearchResultQuery = normalizedQuery,
                            isLiveSearchLoading = false,
                            failedLiveSearchQuery = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            liveSearchPlaces = persistentListOf(),
                            liveSearchResultQuery = normalizedQuery,
                            isLiveSearchLoading = false,
                            failedLiveSearchQuery = normalizedQuery,
                        )
                    },
                )
            }
        }
    }

    private fun searchSubmittedBuildings(query: String) {
        val normalizedQuery = query.trim()
        submittedSearchJob?.cancel()

        if (normalizedQuery.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    searchPlaces = persistentListOf(),
                    searchResultQuery = null,
                    isSubmittedSearchLoading = false,
                    failedSubmittedSearchQuery = null,
                )
            }
            return
        }

        _uiState.update { currentState ->
            val alreadyHasResult = currentState.searchResultQuery == normalizedQuery
            currentState.copy(
                searchPlaces = if (alreadyHasResult) {
                    currentState.searchPlaces
                } else {
                    persistentListOf()
                },
                searchResultQuery = currentState.searchResultQuery.takeIf { alreadyHasResult },
                isSubmittedSearchLoading = true,
                failedSubmittedSearchQuery = null,
            )
        }

        submittedSearchJob = viewModelScope.launch {
            val result = placeRepository.searchPlaceBuildings(normalizedQuery)
            _uiState.update { currentState ->
                if (currentState.submittedSearchQuery != normalizedQuery) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { places ->
                        currentState.copy(
                            searchPlaces = places.toImmutableList(),
                            searchResultQuery = normalizedQuery,
                            isSubmittedSearchLoading = false,
                            failedSubmittedSearchQuery = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            searchPlaces = persistentListOf(),
                            searchResultQuery = normalizedQuery,
                            isSubmittedSearchLoading = false,
                            failedSubmittedSearchQuery = normalizedQuery,
                        )
                    },
                )
            }
        }
    }

    private fun fetchPlaceDetail(
        place: Place,
        source: CampusMapFocusedPlaceSource,
    ) {
        placeDetailJob?.cancel()
        placeDetailCache[place.id]?.let { cachedPlace ->
            _uiState.update { currentState ->
                currentState.copy(
                    focusedPlace = cachedPlace,
                    pendingFocusedPlace = null,
                    focusedPlaceSource = source,
                    failedPlaceDetail = null,
                )
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                pendingFocusedPlace = place,
                focusedPlaceSource = source,
                failedPlaceDetail = null,
            )
        }

        val buildingId = place.id.toLongOrNull()
        if (buildingId == null) {
            _uiState.update { currentState ->
                currentState.copy(
                    pendingFocusedPlace = null,
                    failedPlaceDetail = CampusMapFailedRequest.PlaceDetail(place, source),
                )
            }
            return
        }

        placeDetailJob = viewModelScope.launch {
            val result = placeRepository.getPlaceBuildingDetail(buildingId)
            _uiState.update { currentState ->
                if (currentState.pendingFocusedPlace?.id != place.id) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { detailedPlace ->
                        placeDetailCache[detailedPlace.id] = detailedPlace
                        currentState.copy(
                            focusedPlace = detailedPlace,
                            pendingFocusedPlace = null,
                            focusedPlaceSource = source,
                            failedPlaceDetail = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            pendingFocusedPlace = null,
                            failedPlaceDetail = CampusMapFailedRequest.PlaceDetail(place, source),
                        )
                    },
                )
            }
        }
    }

    internal fun retryFailedRequest(request: CampusMapFailedRequest) {
        when (request) {
            CampusMapFailedRequest.InitialData -> {
                if (_uiState.value.isInitialLoadFailed) fetchInitialData()
            }

            is CampusMapFailedRequest.Category -> {
                val currentState = _uiState.value
                if (
                    currentState.selectedCategory == request.category &&
                    currentState.failedCategory == request.category
                ) {
                    fetchCategoryPlaces(request.category)
                }
            }

            is CampusMapFailedRequest.Search -> if (request.isSubmitted) {
                val currentState = _uiState.value
                if (
                    currentState.submittedSearchQuery == request.query &&
                    currentState.failedSubmittedSearchQuery == request.query
                ) {
                    searchSubmittedBuildings(request.query)
                }
            } else {
                val currentState = _uiState.value
                if (
                    currentState.searchInput.trim() == request.query &&
                    currentState.failedLiveSearchQuery == request.query
                ) {
                    searchLiveBuildings(
                        query = request.query,
                        debounce = false,
                    )
                }
            }

            is CampusMapFailedRequest.PlaceDetail -> {
                if (_uiState.value.failedPlaceDetail == request) {
                    fetchPlaceDetail(
                        place = request.place,
                        source = request.source,
                    )
                }
            }
        }
    }

    private fun CampusMapUiState.withSubmittedSearchQuery(query: String): CampusMapUiState {
        val recentSearch = CampusMapRecentSearch.Query(query)
        return copy(
            focusedPlace = null,
            pendingFocusedPlace = null,
            selectedSearchPlace = null,
            submittedSearchQuery = query,
            selectedCategory = null,
            categoryPlaces = persistentListOf(),
            searchInput = query,
            isCategoryLoading = false,
            isSubmittedSearchLoading = false,
            isLiveSearchLoading = false,
            focusedPlaceSource = null,
            failedCategory = null,
            failedSubmittedSearchQuery = null,
            failedLiveSearchQuery = null,
            failedPlaceDetail = null,
            recentSearches = updateRecentSearches(
                current = recentSearches,
                selected = recentSearch,
            ).toImmutableList(),
        )
    }

    private fun CampusMapUiState.withSelectedSearchPlace(place: Place): CampusMapUiState = copy(
        focusedPlace = null,
        pendingFocusedPlace = null,
        selectedSearchPlace = place,
        submittedSearchQuery = null,
        selectedCategory = null,
        categoryPlaces = persistentListOf(),
        searchPlaces = persistentListOf(),
        searchResultQuery = null,
        liveSearchPlaces = persistentListOf(),
        liveSearchResultQuery = null,
        searchInput = "",
        isCategoryLoading = false,
        isSubmittedSearchLoading = false,
        isLiveSearchLoading = false,
        focusedPlaceSource = CampusMapFocusedPlaceSource.SEARCH_RESULT,
        failedCategory = null,
        failedSubmittedSearchQuery = null,
        failedLiveSearchQuery = null,
        failedPlaceDetail = null,
        recentSearches = updateRecentSearches(
            current = recentSearches,
            selected = CampusMapRecentSearch.PlaceResult(place),
        ).toImmutableList(),
    )

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
