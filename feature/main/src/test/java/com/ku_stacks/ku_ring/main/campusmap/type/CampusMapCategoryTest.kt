package com.ku_stacks.ku_ring.main.campusmap.type

import com.ku_stacks.ku_ring.domain.PlaceCategory
import com.ku_stacks.ku_ring.main.R
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
        assertEquals(CampusMapCategory.BANK_ATM, CampusMapCategory.fromApiName("bank_atm"))
        assertEquals(CampusMapCategory.POST_OFFICE, CampusMapCategory.fromApiName("POST_OFFICE"))
        assertEquals(CampusMapCategory.RESTROOM, CampusMapCategory.fromApiName("restroom"))
        assertEquals(CampusMapCategory.LIBRARY, CampusMapCategory.fromApiName("LIBRARY"))
        assertNull(CampusMapCategory.fromApiName("cultural_facility"))
        assertNull(CampusMapCategory.fromApiName("future_category"))
    }

    @Test
    fun `category matching accepts API codes and Korean aliases`() {
        assertTrue(CampusMapCategory.CAFE.matches("cafe"))
        assertTrue(CampusMapCategory.CAFE.matches("카페"))
        assertTrue(CampusMapCategory.PRINT.matches("복사실"))
        assertTrue(CampusMapCategory.K_CUBE.matches("kcube"))
        assertTrue(CampusMapCategory.BANK_ATM.matches("은행/ATM"))
        assertTrue(CampusMapCategory.POST_OFFICE.matches("우편취급국"))
        assertTrue(CampusMapCategory.RESTROOM.matches("화장실"))
        assertTrue(CampusMapCategory.LIBRARY.matches("열람실"))
    }

    @Test
    fun `ATM and post office API categories use their dedicated icons`() {
        assertEquals(
            R.drawable.ic_campus_map_icon_atm,
            CampusMapCategory.iconRes("bank_atm"),
        )
        assertEquals(
            R.drawable.ic_campus_map_icon_postoffice,
            CampusMapCategory.iconRes("post_office"),
        )
    }

    @Test
    fun `restroom and library API categories use their dedicated icons`() {
        assertEquals(
            R.drawable.ic_campus_map_icon_restroom,
            CampusMapCategory.iconRes("restroom"),
        )
        assertEquals(
            R.drawable.ic_campus_map_icon_library,
            CampusMapCategory.iconRes("library"),
        )
    }

    @Test
    fun `cinema icon is used only for KU Cinema among cultural facilities`() {
        assertEquals(
            R.drawable.ic_campus_map_icon_cinema,
            CampusMapCategory.iconRes(
                category = "cultural_facility",
                facilityName = "예술영화관(KU시네마테크)",
            ),
        )
        assertEquals(
            R.drawable.ic_campus_map_icon_building,
            CampusMapCategory.iconRes(
                category = "cultural_facility",
                facilityName = "학생회관 문화시설",
            ),
        )
    }

    @Test
    fun `server categories are ordered mapped and labeled by API response`() {
        val items = listOf(
            PlaceCategory(name = "printer", korName = "프린터", displayOrder = 3),
            PlaceCategory(name = "future_category", korName = "새 시설", displayOrder = 1),
            PlaceCategory(name = "cafe", korName = "카페", displayOrder = 2),
            PlaceCategory(name = "restaurant", korName = "", displayOrder = 4),
            PlaceCategory(name = "bank_atm", korName = "은행/ATM", displayOrder = 5),
        ).toCampusMapCategoryItems()

        assertEquals(
            listOf(
                CampusMapCategoryItem(CampusMapCategory.CAFE, "카페"),
                CampusMapCategoryItem(CampusMapCategory.PRINT, "프린터"),
                CampusMapCategoryItem(CampusMapCategory.RESTAURANT, "식당"),
                CampusMapCategoryItem(CampusMapCategory.BANK_ATM, "은행/ATM"),
            ),
            items,
        )
    }

}
