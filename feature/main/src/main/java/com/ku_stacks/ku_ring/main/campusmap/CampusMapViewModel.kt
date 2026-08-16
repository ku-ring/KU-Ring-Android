package com.ku_stacks.ku_ring.main.campusmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceSearchResult
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapRecentSearch
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapSearchResult
import com.ku_stacks.ku_ring.main.campusmap.model.groupByCampusBuilding
import com.ku_stacks.ku_ring.main.campusmap.model.toCampusMapSearchResult
import com.ku_stacks.ku_ring.main.campusmap.model.updateRecentSearches
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.ku_stacks.ku_ring.main.campusmap.type.toCampusMapCategoryItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
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
    private var categoryRequestGeneration = 0L
    private var liveSearchRequestGeneration = 0L
    private var submittedSearchRequestGeneration = 0L
    private var placeDetailRequestGeneration = 0L

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
        cancelCategoryRequest()
        cancelLiveSearchRequest()
        cancelSubmittedSearchRequest()
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                selectedSearchPlace = null,
                selectedSearchLabel = null,
                submittedSearchQuery = null,
                selectedCategories = persistentListOf(),
                categoryPlaces = persistentListOf(),
                searchPlaces = persistentListOf(),
                searchCampusPlaces = persistentListOf(),
                searchResultQuery = null,
                liveSearchPlaces = persistentListOf(),
                liveSearchCampusPlaces = persistentListOf(),
                liveSearchResultQuery = null,
                searchInput = "",
                isCategoryLoading = false,
                isSubmittedSearchLoading = false,
                isLiveSearchLoading = false,
                failedCategories = null,
                failedSubmittedSearchQuery = null,
                failedLiveSearchQuery = null,
            )
        }
        fetchPlaceDetail(place, CampusMapFocusedPlaceSource.MAP_MARKER)
    }

    fun clearFocusedPlace() {
        cancelPlaceDetailRequest()
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                pendingFocusedPlace = null,
                selectedSearchPlace = null,
                selectedSearchLabel = null,
                focusedPlaceSource = null,
                failedPlaceDetail = null,
            )
        }
    }

    fun clearActiveSelection() {
        cancelCategoryRequest()
        cancelLiveSearchRequest()
        cancelSubmittedSearchRequest()
        cancelPlaceDetailRequest()
        _uiState.update { currentState ->
            currentState.copy(
                focusedPlace = null,
                pendingFocusedPlace = null,
                selectedSearchPlace = null,
                selectedSearchLabel = null,
                submittedSearchQuery = null,
                selectedCategories = persistentListOf(),
                categoryPlaces = persistentListOf(),
                searchPlaces = persistentListOf(),
                searchCampusPlaces = persistentListOf(),
                searchResultQuery = null,
                liveSearchPlaces = persistentListOf(),
                liveSearchCampusPlaces = persistentListOf(),
                liveSearchResultQuery = null,
                searchInput = "",
                isCategoryLoading = false,
                isSubmittedSearchLoading = false,
                isLiveSearchLoading = false,
                focusedPlaceSource = null,
                failedCategories = null,
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
        val selectedCategories = _uiState.value.selectedCategories
            .let { currentCategories ->
                if (category in currentCategories) {
                    currentCategories.filterNot { it == category }
                } else {
                    currentCategories + category
                }
            }
            .toImmutableList()
        cancelCategoryRequest()
        cancelPlaceDetailRequest()

        if (selectedCategories.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedCategories = persistentListOf(),
                    categoryPlaces = persistentListOf(),
                    focusedPlace = null,
                    pendingFocusedPlace = null,
                    focusedPlaceSource = null,
                    isCategoryLoading = false,
                    failedCategories = null,
                    failedPlaceDetail = null,
                )
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                selectedCategories = selectedCategories,
                categoryPlaces = persistentListOf(),
                focusedPlace = null,
                pendingFocusedPlace = null,
                focusedPlaceSource = null,
                isCategoryLoading = true,
                failedCategories = null,
                failedPlaceDetail = null,
            )
        }
        fetchCategoryPlaces(selectedCategories)
    }

    private fun fetchCategoryPlaces(
        categories: ImmutableList<CampusMapCategory>,
    ) {
        cancelCategoryRequest()
        val requestGeneration = categoryRequestGeneration
        _uiState.update { currentState ->
            currentState.copy(
                isCategoryLoading = true,
                failedCategories = null,
            )
        }
        categoryJob = viewModelScope.launch {
            val result = placeRepository.getPlaceCampusPlaces(
                categories.map(CampusMapCategory::apiName).toTypedArray(),
            )
            _uiState.update { currentState ->
                if (
                    requestGeneration != categoryRequestGeneration ||
                    currentState.selectedCategories != categories
                ) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { facilities ->
                        currentState.copy(
                            categoryPlaces = facilities
                                .groupByCampusBuilding()
                                .toImmutableList(),
                            isCategoryLoading = false,
                            failedCategories = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            categoryPlaces = persistentListOf(),
                            isCategoryLoading = false,
                            failedCategories = categories,
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
            searchLivePlaces(
                query = preparedInput,
                debounce = true,
            )
        }
    }

    fun updateSearchInput(input: String) {
        _uiState.update { currentState ->
            currentState.copy(searchInput = input)
        }
        searchLivePlaces(
            query = input,
            debounce = true,
        )
    }

    fun clearSearchInput() {
        cancelLiveSearchRequest()
        _uiState.update { currentState ->
            currentState.copy(
                searchInput = "",
                liveSearchPlaces = persistentListOf(),
                liveSearchCampusPlaces = persistentListOf(),
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
                    currentState.withSelectedSearchPlace(
                        place = recentSearch.place,
                        label = recentSearch.label,
                        searchResultId = recentSearch.searchResultId,
                    )
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
                searchSubmittedPlaces(recentSearch.query)
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

    internal fun selectSearchResult(result: CampusMapSearchResult) {
        cancelRequestsForCommittedSearch()
        _uiState.update { currentState ->
            currentState.withSelectedSearchResult(result)
        }
        fetchPlaceDetail(result.place, CampusMapFocusedPlaceSource.SEARCH_RESULT)
    }

    fun selectSearchPlace(place: Place) = selectSearchResult(place.toCampusMapSearchResult())

    internal fun focusSearchResult(result: CampusMapSearchResult) {
        fetchPlaceDetail(result.place, CampusMapFocusedPlaceSource.SEARCH_RESULT)
    }

    fun focusSearchResultPlace(place: Place) {
        fetchPlaceDetail(place, CampusMapFocusedPlaceSource.SEARCH_RESULT)
    }

    fun submitSearch() {
        val currentState = _uiState.value
        val submittedQuery = currentState.searchInput.trim()
        if (submittedQuery.isEmpty()) return
        val reusableLiveSearchResult = if (
            currentState.liveSearchResultQuery == submittedQuery &&
            !currentState.isLiveSearchLoading &&
            currentState.failedLiveSearchQuery != submittedQuery
        ) {
            PlaceSearchResult(
                buildings = currentState.liveSearchPlaces,
                campusPlaces = currentState.liveSearchCampusPlaces,
            )
        } else {
            null
        }

        cancelRequestsForCommittedSearch()
        _uiState.update { currentState ->
            val submittedState = currentState.withSubmittedSearchQuery(submittedQuery)
            if (reusableLiveSearchResult == null) {
                submittedState
            } else {
                submittedState.copy(
                    searchPlaces = reusableLiveSearchResult.buildings.toImmutableList(),
                    searchCampusPlaces = reusableLiveSearchResult.campusPlaces.toImmutableList(),
                    searchResultQuery = submittedQuery,
                )
            }
        }
        if (reusableLiveSearchResult == null) {
            searchSubmittedPlaces(submittedQuery)
        }
    }

    private fun cancelRequestsForCommittedSearch() {
        cancelCategoryRequest()
        cancelLiveSearchRequest()
        cancelSubmittedSearchRequest()
        cancelPlaceDetailRequest()
    }

    private fun cancelCategoryRequest() {
        categoryRequestGeneration += 1
        categoryJob?.cancel()
    }

    private fun cancelLiveSearchRequest() {
        liveSearchRequestGeneration += 1
        liveSearchJob?.cancel()
    }

    private fun cancelSubmittedSearchRequest() {
        submittedSearchRequestGeneration += 1
        submittedSearchJob?.cancel()
    }

    private fun cancelPlaceDetailRequest() {
        placeDetailRequestGeneration += 1
        placeDetailJob?.cancel()
    }

    private fun searchLivePlaces(
        query: String,
        debounce: Boolean,
    ) {
        val normalizedQuery = query.trim()
        cancelLiveSearchRequest()
        val requestGeneration = liveSearchRequestGeneration

        if (normalizedQuery.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    liveSearchPlaces = persistentListOf(),
                    liveSearchCampusPlaces = persistentListOf(),
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
                liveSearchCampusPlaces = if (alreadyHasResult) {
                    currentState.liveSearchCampusPlaces
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

            val result = placeRepository.searchPlaces(normalizedQuery)
            _uiState.update { currentState ->
                if (
                    requestGeneration != liveSearchRequestGeneration ||
                    currentState.searchInput.trim() != normalizedQuery
                ) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { searchResult ->
                        currentState.copy(
                            liveSearchPlaces = searchResult.buildings.toImmutableList(),
                            liveSearchCampusPlaces = searchResult.campusPlaces.toImmutableList(),
                            liveSearchResultQuery = normalizedQuery,
                            isLiveSearchLoading = false,
                            failedLiveSearchQuery = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            liveSearchPlaces = persistentListOf(),
                            liveSearchCampusPlaces = persistentListOf(),
                            liveSearchResultQuery = normalizedQuery,
                            isLiveSearchLoading = false,
                            failedLiveSearchQuery = normalizedQuery,
                        )
                    },
                )
            }
        }
    }

    private fun searchSubmittedPlaces(query: String) {
        val normalizedQuery = query.trim()
        cancelSubmittedSearchRequest()
        val requestGeneration = submittedSearchRequestGeneration

        if (normalizedQuery.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    searchPlaces = persistentListOf(),
                    searchCampusPlaces = persistentListOf(),
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
                searchCampusPlaces = if (alreadyHasResult) {
                    currentState.searchCampusPlaces
                } else {
                    persistentListOf()
                },
                searchResultQuery = currentState.searchResultQuery.takeIf { alreadyHasResult },
                isSubmittedSearchLoading = true,
                failedSubmittedSearchQuery = null,
            )
        }

        submittedSearchJob = viewModelScope.launch {
            val result = placeRepository.searchPlaces(normalizedQuery)
            _uiState.update { currentState ->
                if (
                    requestGeneration != submittedSearchRequestGeneration ||
                    currentState.submittedSearchQuery != normalizedQuery
                ) {
                    return@update currentState
                }

                result.fold(
                    onSuccess = { searchResult ->
                        currentState.copy(
                            searchPlaces = searchResult.buildings.toImmutableList(),
                            searchCampusPlaces = searchResult.campusPlaces.toImmutableList(),
                            searchResultQuery = normalizedQuery,
                            isSubmittedSearchLoading = false,
                            failedSubmittedSearchQuery = null,
                        )
                    },
                    onFailure = {
                        currentState.copy(
                            searchPlaces = persistentListOf(),
                            searchCampusPlaces = persistentListOf(),
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
        cancelPlaceDetailRequest()
        val requestGeneration = placeDetailRequestGeneration
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
                if (
                    requestGeneration != placeDetailRequestGeneration ||
                    currentState.pendingFocusedPlace?.id != place.id
                ) {
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
                    currentState.selectedCategories == request.categories &&
                    currentState.failedCategories == request.categories
                ) {
                    fetchCategoryPlaces(request.categories)
                }
            }

            is CampusMapFailedRequest.Search -> if (request.isSubmitted) {
                val currentState = _uiState.value
                if (
                    currentState.submittedSearchQuery == request.query &&
                    currentState.failedSubmittedSearchQuery == request.query
                ) {
                    searchSubmittedPlaces(request.query)
                }
            } else {
                val currentState = _uiState.value
                if (
                    currentState.searchInput.trim() == request.query &&
                    currentState.failedLiveSearchQuery == request.query
                ) {
                    searchLivePlaces(
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
            selectedSearchLabel = null,
            submittedSearchQuery = query,
            selectedCategories = persistentListOf(),
            categoryPlaces = persistentListOf(),
            searchInput = query,
            isCategoryLoading = false,
            isSubmittedSearchLoading = false,
            isLiveSearchLoading = false,
            focusedPlaceSource = null,
            failedCategories = null,
            failedSubmittedSearchQuery = null,
            failedLiveSearchQuery = null,
            failedPlaceDetail = null,
            recentSearches = updateRecentSearches(
                current = recentSearches,
                selected = recentSearch,
            ).toImmutableList(),
        )
    }

    private fun CampusMapUiState.withSelectedSearchResult(
        result: CampusMapSearchResult,
    ): CampusMapUiState = withSelectedSearchPlace(
        place = result.place,
        label = result.title,
        searchResultId = result.id,
    )

    private fun CampusMapUiState.withSelectedSearchPlace(
        place: Place,
        label: String = place.name,
        searchResultId: String = "place:${place.id}",
    ): CampusMapUiState = copy(
        focusedPlace = null,
        pendingFocusedPlace = null,
        selectedSearchPlace = place,
        selectedSearchLabel = label,
        submittedSearchQuery = null,
        selectedCategories = persistentListOf(),
        categoryPlaces = persistentListOf(),
        searchPlaces = persistentListOf(),
        searchCampusPlaces = persistentListOf(),
        searchResultQuery = null,
        liveSearchPlaces = persistentListOf(),
        liveSearchCampusPlaces = persistentListOf(),
        liveSearchResultQuery = null,
        searchInput = "",
        isCategoryLoading = false,
        isSubmittedSearchLoading = false,
        isLiveSearchLoading = false,
        focusedPlaceSource = CampusMapFocusedPlaceSource.SEARCH_RESULT,
        failedCategories = null,
        failedSubmittedSearchQuery = null,
        failedLiveSearchQuery = null,
        failedPlaceDetail = null,
        recentSearches = updateRecentSearches(
            current = recentSearches,
            selected = CampusMapRecentSearch.PlaceResult(
                place = place,
                label = label,
                searchResultId = searchResultId,
            ),
        ).toImmutableList(),
    )

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
