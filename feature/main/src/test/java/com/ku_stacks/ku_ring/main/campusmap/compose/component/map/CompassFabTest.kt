package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CompassFabTest {

    @Test
    fun `compass is hidden when the map is aligned to north`() {
        assertFalse(shouldShowCompass(0.0))
        assertFalse(shouldShowCompass(360.0))
        assertFalse(shouldShowCompass(1.9))
        assertFalse(shouldShowCompass(358.1))
    }

    @Test
    fun `compass is visible when the map rotates beyond the tolerance`() {
        assertTrue(shouldShowCompass(2.1))
        assertTrue(shouldShowCompass(357.9))
        assertTrue(shouldShowCompass(180.0))
    }

    @Test
    fun `angular distance uses the shortest path to north`() {
        assertEquals(1.0, angularDistanceFromNorth(359.0))
        assertEquals(1.0, angularDistanceFromNorth(-1.0))
        assertEquals(90.0, angularDistanceFromNorth(270.0))
    }

    @Test
    fun `compass rotates opposite to the map bearing`() {
        assertEquals(-45f, compassRotationDegrees(45.0))
        assertEquals(45f, compassRotationDegrees(315.0))
        assertEquals(0f, compassRotationDegrees(720.0))
    }
}
