package com.ku_stacks.ku_ring.main.campusmap

import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceBuilding
import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CampusMapViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load exposes buildings and API ordered categories`() = runTest {
        val repository = FakePlaceRepository().apply {
            buildingsResponse = {
                delay(100)
                Result.success(listOf(librarySummary))
            }
            categoriesResponse = {
                delay(50)
                Result.success(
                    listOf(
                        PlaceCategory("printer", "프린터", 2),
                        PlaceCategory("cafe", "교내 카페", 1),
                    ),
                )
            }
        }
        val viewModel = CampusMapViewModel(repository)

        runCurrent()
        assertTrue(viewModel.uiState.value.isInitialLoading)

        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertFalse(isInitialLoading)
            assertFalse(isInitialLoadFailed)
            assertNull(mapFailedRequest)
            assertEquals(listOf(librarySummary), campusPlaces)
            assertEquals(
                listOf(CampusMapCategory.CAFE, CampusMapCategory.PRINT),
                categories.map { it.category },
            )
        }
        assertEquals(1, repository.buildingsCallCount)
        assertEquals(1, repository.categoriesCallCount)
    }

    @Test
    fun `typing waits for debounce before searching the API`() = runTest {
        val repository = FakePlaceRepository().apply {
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("도서관")
        runCurrent()

        assertTrue(viewModel.uiState.value.isLiveSearchLoading)
        assertTrue(repository.searchQueries.isEmpty())

        advanceTimeBy(299)
        runCurrent()
        assertTrue(repository.searchQueries.isEmpty())

        advanceTimeBy(1)
        runCurrent()

        assertEquals(listOf("도서관"), repository.searchQueries)
        assertEquals(listOf(librarySummary), viewModel.uiState.value.liveSearchPlaces)
        assertEquals("도서관", viewModel.uiState.value.liveSearchResultQuery)
        assertFalse(viewModel.uiState.value.isLiveSearchLoading)
    }

    @Test
    fun `submitting a query cancels debounce and searches immediately`() = runTest {
        val repository = FakePlaceRepository().apply {
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("  도서관  ")
        viewModel.submitSearch()
        runCurrent()

        with(viewModel.uiState.value) {
            assertEquals("도서관", submittedSearchQuery)
            assertEquals("도서관", searchResultQuery)
            assertEquals(listOf(librarySummary), visiblePlaces)
            assertEquals(listOf("도서관"), recentSearches.map { it.label })
        }
        assertEquals(listOf("도서관"), repository.searchQueries)
    }

    @Test
    fun `submitting a completed live search reuses its result without another API call`() = runTest {
        val repository = FakePlaceRepository().apply {
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("도서관")
        advanceUntilIdle()
        viewModel.submitSearch()
        runCurrent()

        with(viewModel.uiState.value) {
            assertEquals("도서관", submittedSearchQuery)
            assertEquals("도서관", searchResultQuery)
            assertEquals(listOf(librarySummary), searchPlaces)
            assertEquals(listOf(librarySummary), visiblePlaces)
            assertFalse(isSubmittedSearchLoading)
        }
        assertEquals(listOf("도서관"), repository.searchQueries)
    }

    @Test
    fun `reused live result is not replaced by a canceled submitted request`() = runTest {
        val stalePlace = place(id = "1", name = "오래된 검색 결과")
        val freshPlace = place(id = "2", name = "최신 검색 결과")
        var requestCount = 0
        val repository = FakePlaceRepository().apply {
            searchResponse = {
                requestCount += 1
                if (requestCount == 1) {
                    withContext(NonCancellable) { delay(1_000) }
                    Result.success(listOf(stalePlace))
                } else {
                    Result.success(listOf(freshPlace))
                }
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("반복")
        viewModel.submitSearch()
        runCurrent()
        viewModel.prepareSearchInput()
        advanceTimeBy(300)
        runCurrent()

        assertEquals(listOf(freshPlace), viewModel.uiState.value.liveSearchPlaces)

        viewModel.submitSearch()
        runCurrent()

        assertEquals(listOf(freshPlace), viewModel.uiState.value.searchPlaces)

        advanceUntilIdle()

        assertEquals(listOf(freshPlace), viewModel.uiState.value.searchPlaces)
        assertEquals(listOf("반복", "반복"), repository.searchQueries)
    }

    @Test
    fun `submitting after a failed live search retries as a submitted search`() = runTest {
        var attempt = 0
        val repository = FakePlaceRepository().apply {
            searchResponse = {
                attempt += 1
                if (attempt == 1) {
                    Result.failure(IllegalStateException("live search"))
                } else {
                    Result.success(listOf(librarySummary))
                }
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("도서관")
        advanceUntilIdle()
        assertEquals("도서관", viewModel.uiState.value.failedLiveSearchQuery)

        viewModel.submitSearch()
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals("도서관", submittedSearchQuery)
            assertEquals("도서관", searchResultQuery)
            assertEquals(listOf(librarySummary), searchPlaces)
            assertNull(failedSubmittedSearchQuery)
        }
        assertEquals(listOf("도서관", "도서관"), repository.searchQueries)
    }

    @Test
    fun `live search cannot replace a submitted result when returning to the map`() = runTest {
        val businessBuilding = place(id = "11", name = "경영관")
        val repository = FakePlaceRepository().apply {
            searchResponse = { query ->
                Result.success(
                    when (query) {
                        "도서관" -> listOf(librarySummary)
                        "경영관" -> listOf(businessBuilding)
                        else -> emptyList()
                    },
                )
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("도서관")
        viewModel.submitSearch()
        runCurrent()

        viewModel.prepareSearchInput()
        viewModel.updateSearchInput("경영관")
        advanceTimeBy(300)
        runCurrent()

        with(viewModel.uiState.value) {
            assertEquals("도서관", submittedSearchQuery)
            assertEquals("도서관", searchResultQuery)
            assertEquals(listOf(librarySummary), searchPlaces)
            assertEquals(listOf(librarySummary), visiblePlaces)
            assertEquals("도서관", activeSearchText)
            assertEquals("경영관", liveSearchResultQuery)
            assertEquals(listOf(businessBuilding), liveSearchPlaces)
        }
        assertEquals(listOf("도서관", "경영관"), repository.searchQueries)
    }

    @Test
    fun `selecting a category fetches its API code and groups facilities by building`() = runTest {
        val cafe = facility(
            id = 101,
            name = "카페 쿠",
            category = "cafe",
            building = libraryBuilding,
        )
        val repository = FakePlaceRepository().apply {
            campusPlacesResponse = { Result.success(listOf(cafe)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSelectedCategory(CampusMapCategory.CAFE)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(CampusMapCategory.CAFE, selectedCategory)
            assertFalse(isCategoryLoading)
            assertEquals(1, categoryPlaces.size)
            assertEquals(libraryBuilding.id.toString(), categoryPlaces.single().id)
            assertEquals(listOf(cafe), categoryPlaces.single().facilities)
        }
        assertEquals(listOf(listOf("cafe")), repository.campusPlaceCategories)
    }

    @Test
    fun `submitting search cancels category loading and ignores its late response`() = runTest {
        val cafe = facility(
            id = 101,
            name = "카페 쿠",
            category = "cafe",
            building = libraryBuilding,
        )
        val repository = FakePlaceRepository().apply {
            campusPlacesResponse = {
                withContext(NonCancellable) { delay(1_000) }
                Result.success(listOf(cafe))
            }
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSelectedCategory(CampusMapCategory.CAFE)
        runCurrent()
        assertTrue(viewModel.uiState.value.isCategoryLoading)

        viewModel.updateSearchInput("도서관")
        viewModel.submitSearch()

        with(viewModel.uiState.value) {
            assertNull(selectedCategory)
            assertFalse(isCategoryLoading)
            assertTrue(categoryPlaces.isEmpty())
        }

        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertNull(selectedCategory)
            assertFalse(isCategoryLoading)
            assertTrue(categoryPlaces.isEmpty())
            assertEquals("도서관", submittedSearchQuery)
            assertEquals(listOf(librarySummary), searchPlaces)
        }
    }

    @Test
    fun `selecting a place cancels category loading and ignores its late response`() = runTest {
        val cafe = facility(
            id = 101,
            name = "카페 쿠",
            category = "cafe",
            building = libraryBuilding,
        )
        val repository = FakePlaceRepository().apply {
            campusPlacesResponse = {
                withContext(NonCancellable) { delay(1_000) }
                Result.success(listOf(cafe))
            }
            detailResponse = { Result.success(libraryDetail) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSelectedCategory(CampusMapCategory.CAFE)
        runCurrent()
        assertTrue(viewModel.uiState.value.isCategoryLoading)

        viewModel.selectSearchPlace(librarySummary)

        with(viewModel.uiState.value) {
            assertNull(selectedCategory)
            assertFalse(isCategoryLoading)
            assertTrue(categoryPlaces.isEmpty())
        }

        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertNull(selectedCategory)
            assertFalse(isCategoryLoading)
            assertTrue(categoryPlaces.isEmpty())
            assertEquals(libraryDetail, focusedPlace)
        }
    }

    @Test
    fun `selecting a place fetches detail and reuses the cached response`() = runTest {
        val repository = FakePlaceRepository().apply {
            detailResponse = {
                delay(100)
                Result.success(libraryDetail)
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.selectSearchPlace(librarySummary)
        runCurrent()

        assertEquals(librarySummary, viewModel.uiState.value.pendingFocusedPlace)

        advanceUntilIdle()
        assertEquals(libraryDetail, viewModel.uiState.value.focusedPlace)
        assertEquals(CampusMapFocusedPlaceSource.SEARCH_RESULT, viewModel.uiState.value.focusedPlaceSource)
        assertEquals(listOf(10L), repository.detailBuildingIds)

        viewModel.clearFocusedPlace()
        viewModel.focusSearchResultPlace(librarySummary)

        assertEquals(libraryDetail, viewModel.uiState.value.focusedPlace)
        assertEquals(listOf(10L), repository.detailBuildingIds)
    }

    @Test
    fun `successful live search does not clear an initial load error`() = runTest {
        val repository = FakePlaceRepository().apply {
            buildingsResponse = { Result.failure(IllegalStateException("initial")) }
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("도서관")
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(listOf(librarySummary), liveSearchPlaces)
            assertTrue(isInitialLoadFailed)
            assertEquals(CampusMapFailedRequest.InitialData, mapFailedRequest)
        }
    }

    @Test
    fun `successful live search does not clear a category error`() = runTest {
        val repository = FakePlaceRepository().apply {
            campusPlacesResponse = { Result.failure(IllegalStateException("category")) }
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSelectedCategory(CampusMapCategory.CAFE)
        advanceUntilIdle()
        viewModel.updateSearchInput("도서관")
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(listOf(librarySummary), liveSearchPlaces)
            assertEquals(CampusMapCategory.CAFE, failedCategory)
            assertEquals(
                CampusMapFailedRequest.Category(CampusMapCategory.CAFE),
                mapFailedRequest,
            )
        }
    }

    @Test
    fun `successful category request does not clear a submitted search error`() = runTest {
        val cafe = facility(
            id = 101,
            name = "카페 쿠",
            category = "cafe",
            building = libraryBuilding,
        )
        val repository = FakePlaceRepository().apply {
            searchResponse = { Result.failure(IllegalStateException("search")) }
            campusPlacesResponse = { Result.success(listOf(cafe)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("도서관")
        viewModel.submitSearch()
        advanceUntilIdle()
        viewModel.updateSelectedCategory(CampusMapCategory.CAFE)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(1, categoryPlaces.size)
            assertEquals("도서관", failedSubmittedSearchQuery)
            assertEquals(
                CampusMapFailedRequest.Search("도서관", isSubmitted = true),
                mapFailedRequest,
            )
        }
    }

    @Test
    fun `successful live search does not clear a detail error`() = runTest {
        val failedDetail = CampusMapFailedRequest.PlaceDetail(
            librarySummary,
            CampusMapFocusedPlaceSource.SEARCH_RESULT,
        )
        val repository = FakePlaceRepository().apply {
            detailResponse = { Result.failure(IllegalStateException("detail")) }
            searchResponse = { Result.success(listOf(librarySummary)) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.focusSearchResultPlace(librarySummary)
        advanceUntilIdle()
        viewModel.updateSearchInput("도서관")
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(listOf(librarySummary), liveSearchPlaces)
            assertEquals(failedDetail, failedPlaceDetail)
            assertEquals(failedDetail, mapFailedRequest)
        }
    }

    @Test
    fun `retry ignores requests that are no longer active`() = runTest {
        val repository = FakePlaceRepository()
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.retryFailedRequest(CampusMapFailedRequest.InitialData)
        viewModel.retryFailedRequest(CampusMapFailedRequest.Category(CampusMapCategory.CAFE))
        viewModel.retryFailedRequest(
            CampusMapFailedRequest.Search("도서관", isSubmitted = true),
        )
        viewModel.retryFailedRequest(
            CampusMapFailedRequest.Search("경영관", isSubmitted = false),
        )
        viewModel.retryFailedRequest(
            CampusMapFailedRequest.PlaceDetail(
                librarySummary,
                CampusMapFocusedPlaceSource.MAP_MARKER,
            ),
        )
        runCurrent()

        assertEquals(1, repository.buildingsCallCount)
        assertEquals(1, repository.categoriesCallCount)
        assertTrue(repository.campusPlaceCategories.isEmpty())
        assertTrue(repository.searchQueries.isEmpty())
        assertTrue(repository.detailBuildingIds.isEmpty())
    }

    @Test
    fun `failed initial load can be retried`() = runTest {
        val repository = FakePlaceRepository().apply {
            buildingsResponse = { Result.failure(IllegalStateException("network")) }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isInitialLoadFailed)
        assertEquals(CampusMapFailedRequest.InitialData, viewModel.uiState.value.mapFailedRequest)

        repository.buildingsResponse = { Result.success(listOf(librarySummary)) }
        viewModel.retryFailedRequest(CampusMapFailedRequest.InitialData)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isInitialLoadFailed)
        assertNull(viewModel.uiState.value.mapFailedRequest)
        assertEquals(listOf(librarySummary), viewModel.uiState.value.campusPlaces)
        assertEquals(2, repository.buildingsCallCount)
        assertEquals(2, repository.categoriesCallCount)
    }

    @Test
    fun `failed detail request can be retried for the same building`() = runTest {
        var shouldFail = true
        val repository = FakePlaceRepository().apply {
            detailResponse = {
                if (shouldFail) {
                    Result.failure(IllegalStateException("network"))
                } else {
                    Result.success(libraryDetail)
                }
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.focusMapPlace(librarySummary)
        advanceUntilIdle()

        val failedRequest = CampusMapFailedRequest.PlaceDetail(
            librarySummary,
            CampusMapFocusedPlaceSource.MAP_MARKER,
        )
        assertEquals(failedRequest, viewModel.uiState.value.failedPlaceDetail)

        shouldFail = false
        viewModel.retryFailedRequest(failedRequest)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.failedPlaceDetail)
        assertEquals(libraryDetail, viewModel.uiState.value.focusedPlace)
        assertEquals(listOf(10L, 10L), repository.detailBuildingIds)
    }

    @Test
    fun `a late response from an old query cannot replace the current search`() = runTest {
        val oldPlace = place(id = "1", name = "이전 검색 결과")
        val newPlace = place(id = "2", name = "최신 검색 결과")
        val repository = FakePlaceRepository().apply {
            searchResponse = { query ->
                when (query) {
                    "이전" -> {
                        withContext(NonCancellable) { delay(1_000) }
                        Result.success(listOf(oldPlace))
                    }

                    "최신" -> {
                        delay(10)
                        Result.success(listOf(newPlace))
                    }

                    else -> Result.success(emptyList())
                }
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("이전")
        advanceTimeBy(300)
        runCurrent()
        assertEquals(listOf("이전"), repository.searchQueries)

        viewModel.updateSearchInput("최신")
        advanceTimeBy(300)
        runCurrent()
        advanceTimeBy(10)
        runCurrent()

        assertEquals(listOf(newPlace), viewModel.uiState.value.liveSearchPlaces)
        assertEquals("최신", viewModel.uiState.value.liveSearchResultQuery)

        advanceUntilIdle()

        assertEquals(listOf(newPlace), viewModel.uiState.value.liveSearchPlaces)
        assertEquals("최신", viewModel.uiState.value.liveSearchResultQuery)
        assertEquals(listOf("이전", "최신"), repository.searchQueries)
    }

    @Test
    fun `a late response cannot replace a newer request for the same query`() = runTest {
        val stalePlace = place(id = "1", name = "오래된 검색 결과")
        val freshPlace = place(id = "2", name = "최신 검색 결과")
        var firstRequest = true
        val repository = FakePlaceRepository().apply {
            searchResponse = { query ->
                when {
                    query == "반복" && firstRequest -> {
                        firstRequest = false
                        withContext(NonCancellable) { delay(1_000) }
                        Result.success(listOf(stalePlace))
                    }

                    query == "반복" -> {
                        delay(10)
                        Result.success(listOf(freshPlace))
                    }

                    else -> Result.success(emptyList())
                }
            }
        }
        val viewModel = CampusMapViewModel(repository)
        advanceUntilIdle()

        viewModel.updateSearchInput("반복")
        advanceTimeBy(300)
        runCurrent()
        viewModel.updateSearchInput("다른 검색")
        viewModel.updateSearchInput("반복")
        advanceTimeBy(300)
        runCurrent()
        advanceTimeBy(10)
        runCurrent()

        assertEquals(listOf(freshPlace), viewModel.uiState.value.liveSearchPlaces)
        assertEquals("반복", viewModel.uiState.value.liveSearchResultQuery)

        advanceUntilIdle()

        assertEquals(listOf(freshPlace), viewModel.uiState.value.liveSearchPlaces)
        assertEquals("반복", viewModel.uiState.value.liveSearchResultQuery)
        assertEquals(listOf("반복", "반복"), repository.searchQueries)
    }

    private class FakePlaceRepository : PlaceRepository {
        var buildingsResponse: suspend () -> Result<List<Place>> = { Result.success(emptyList()) }
        var categoriesResponse: suspend () -> Result<List<PlaceCategory>> = {
            Result.success(emptyList())
        }
        var searchResponse: suspend (String) -> Result<List<Place>> = {
            Result.success(emptyList())
        }
        var campusPlacesResponse: suspend (Array<String>) -> Result<List<PlaceFacility>> = {
            Result.success(emptyList())
        }
        var detailResponse: suspend (Long) -> Result<Place> = {
            Result.failure(NoSuchElementException("No detail response configured"))
        }

        var buildingsCallCount = 0
            private set
        var categoriesCallCount = 0
            private set
        val searchQueries = mutableListOf<String>()
        val campusPlaceCategories = mutableListOf<List<String>>()
        val detailBuildingIds = mutableListOf<Long>()

        override suspend fun getPlaceBuildingDetail(buildingId: Long): Result<Place> {
            detailBuildingIds += buildingId
            return detailResponse(buildingId)
        }

        override suspend fun searchPlaceBuildings(keyword: String): Result<List<Place>> {
            searchQueries += keyword
            return searchResponse(keyword)
        }

        override suspend fun getPlaceBuildings(): Result<List<Place>> {
            buildingsCallCount += 1
            return buildingsResponse()
        }

        override suspend fun getPlaceCampusPlaces(
            categories: Array<String>,
        ): Result<List<PlaceFacility>> {
            campusPlaceCategories += categories.toList()
            return campusPlacesResponse(categories)
        }

        override suspend fun getPlaceCategories(): Result<List<PlaceCategory>> {
            categoriesCallCount += 1
            return categoriesResponse()
        }
    }

    private companion object {
        val libraryBuilding = PlaceBuilding(
            id = 10,
            name = "상허기념도서관",
            address = "서울특별시 광진구 능동로 120",
            latitude = 37.542366,
            longitude = 127.076846,
        )

        val librarySummary = place(
            id = libraryBuilding.id.toString(),
            name = libraryBuilding.name,
        )

        val libraryDetail = librarySummary.copy(
            facilities = listOf(
                facility(
                    id = 100,
                    name = "K-CUBE",
                    category = "kcube",
                    building = libraryBuilding,
                ),
            ),
        )

        fun place(
            id: String,
            name: String,
        ) = Place(
            id = id,
            name = name,
            category = "건물",
            address = "서울특별시 광진구 능동로 120",
            latitude = 37.542366,
            longitude = 127.076846,
            priority = Place.Priority.HIGH,
        )

        fun facility(
            id: Long,
            name: String,
            category: String,
            building: PlaceBuilding,
        ) = PlaceFacility(
            id = id,
            name = name,
            category = category,
            categoryKor = category,
            building = building,
        )
    }
}
