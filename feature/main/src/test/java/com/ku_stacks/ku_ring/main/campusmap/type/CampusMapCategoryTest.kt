package com.ku_stacks.ku_ring.main.campusmap.type

import com.ku_stacks.ku_ring.domain.PlaceCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CampusMapCategoryTest {

    @Test
    fun `API category names map case-insensitively to supported UI categories`() {
        assertEquals(CampusMapCategory.CAFE, CampusMapCategory.fromApiName("cafe"))
        assertEquals(CampusMapCategory.STORE, CampusMapCategory.fromApiName("CONVENIENCE_STORE"))
        assertEquals(CampusMapCategory.K_CUBE, CampusMapCategory.fromApiName("KCube"))
        assertNull(CampusMapCategory.fromApiName("future_category"))
    }

    @Test
    fun `category matching accepts API codes and Korean aliases`() {
        assertTrue(CampusMapCategory.CAFE.matches("cafe"))
        assertTrue(CampusMapCategory.CAFE.matches("카페"))
        assertTrue(CampusMapCategory.PRINT.matches("복사실"))
        assertTrue(CampusMapCategory.K_CUBE.matches("kcube"))
    }

    @Test
    fun `server categories are ordered mapped and labeled by API response`() {
        val items = listOf(
            PlaceCategory(name = "printer", korName = "프린터", displayOrder = 3),
            PlaceCategory(name = "future_category", korName = "새 시설", displayOrder = 1),
            PlaceCategory(name = "cafe", korName = "카페", displayOrder = 2),
            PlaceCategory(name = "restaurant", korName = "", displayOrder = 4),
        ).toCampusMapCategoryItems()

        assertEquals(
            listOf(
                CampusMapCategoryItem(CampusMapCategory.CAFE, "카페"),
                CampusMapCategoryItem(CampusMapCategory.PRINT, "프린터"),
                CampusMapCategoryItem(CampusMapCategory.RESTAURANT, "식당"),
            ),
            items,
        )
    }

}
