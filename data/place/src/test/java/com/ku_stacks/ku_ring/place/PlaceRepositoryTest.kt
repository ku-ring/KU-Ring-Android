package com.ku_stacks.ku_ring.place

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.place.repository.PlaceRepository
import com.ku_stacks.ku_ring.place.datasource.PlaceDataSource
import com.ku_stacks.ku_ring.place.repository.PlaceRepositoryImpl
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlaceRepositoryTest {
    private lateinit var dataSource: PlaceDataSource
    private lateinit var repository: PlaceRepository
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)

        dataSource = PlaceDataSource(context, testDispatcher)
        repository = PlaceRepositoryImpl(dataSource)
    }

    @Test
    fun `get places from json file in assets`() = runTest(testDispatcher) {
        // given
        val expect = Place(
            id = "KU스포츠광장",
            name = "KU스포츠광장",
            category = "체육시설",
            address = "서울특별시 광진구 능동로 120",
            latitude = 37.544361,
            longitude = 127.078199,
            priority = Place.Priority.MIDDLE,
        )

        // when
        val result = repository.getPlaces()[0]

        // then
        assertTrue(expect == result)
    }
}