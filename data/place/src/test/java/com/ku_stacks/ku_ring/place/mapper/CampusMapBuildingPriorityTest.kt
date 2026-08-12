package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place
import org.junit.Assert.assertEquals
import org.junit.Test

class CampusMapBuildingPriorityTest {
    @Test
    fun `campus buildings map to configured marker priorities`() {
        val buildingNamesByPriority = mapOf(
            Place.Priority.HIGH to listOf(
                "경영관",
                "상허연구관",
                "상허기념도서관",
                "산학협동관",
                "새천년관",
                "학생회관",
                "공학관",
                "쿨하우스",
            ),
            Place.Priority.MIDDLE to listOf(
                "행정관",
                "교육과학관",
                "예술문화관",
                "법학관",
                "의생명과학연구관",
                "생명과학관",
                "동물생명과학관",
                "수의학관",
                "건축관",
                "해봉부동산학관",
                "인문학관",
                "신공학관",
                "과학관",
                "창의관",
                "제2학생회관",
            ),
            Place.Priority.LOW to listOf(
                "언어교육원",
                "박물관",
                "입학정보관",
                "KU기술혁신관",
                "실내체육관",
                "일우헌",
                "중장비실험동",
                "공예관",
                "생명과학관 부속동",
                "동물병원 별관",
                "교육연수원",
            ),
        )

        buildingNamesByPriority.forEach { (priority, buildingNames) ->
            buildingNames.forEach { buildingName ->
                assertEquals(priority, buildingName.toCampusMapBuildingPriority())
            }
        }
    }

    @Test
    fun `unknown buildings use the lowest marker priority`() {
        assertEquals(Place.Priority.LOW, "새 건물".toCampusMapBuildingPriority())
    }

    @Test
    fun `coolhouse halls use the first marker priority`() {
        val coolhouseHalls = listOf(
            "쿨하우스 글로벌홀",
            "쿨하우스 드림홀",
            "쿨하우스 레이크홀",
            "쿨하우스 비전홀",
            "쿨하우스 프론티어홀",
        )

        coolhouseHalls.forEach { buildingName ->
            assertEquals(Place.Priority.HIGH, buildingName.toCampusMapBuildingPriority())
        }
    }

    @Test
    fun `display order maps to marker priority`() {
        assertEquals(Place.Priority.HIGH, 1.toCampusMapBuildingPriority("새 건물"))
        assertEquals(Place.Priority.MIDDLE, 2.toCampusMapBuildingPriority("새 건물"))
        assertEquals(Place.Priority.LOW, 3.toCampusMapBuildingPriority("새 건물"))
    }

    @Test
    fun `missing or unknown display order falls back to building name`() {
        assertEquals(Place.Priority.HIGH, null.toCampusMapBuildingPriority("경영관"))
        assertEquals(Place.Priority.LOW, 4.toCampusMapBuildingPriority("새 건물"))
    }
}
