package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
import org.junit.Assert.assertEquals
import org.junit.Test

class CampusMapBuildingPriorityTest {
    @Test
    fun `display order maps to marker priority`() {
        assertEquals(Place.Priority.HIGH, 1.toCampusMapBuildingPriority())
        assertEquals(Place.Priority.MIDDLE, 2.toCampusMapBuildingPriority())
        assertEquals(Place.Priority.LOW, 3.toCampusMapBuildingPriority())
    }

    @Test
    fun `missing or unsupported display order uses the lowest marker priority`() {
        listOf<Int?>(null, -1, 0, 4).forEach { displayOrder ->
            assertEquals(Place.Priority.LOW, displayOrder.toCampusMapBuildingPriority())
        }
    }
}
