package com.ku_stacks.ku_ring.place.mapper

import com.ku_stacks.ku_ring.domain.Place

private val CAMPUS_MAP_BUILDING_PRIORITY_BY_NAME = mapOf(
    "행정관" to Place.Priority.MIDDLE,
    "경영관" to Place.Priority.HIGH,
    "상허연구관" to Place.Priority.HIGH,
    "교육과학관" to Place.Priority.MIDDLE,
    "예술문화관" to Place.Priority.MIDDLE,
    "언어교육원" to Place.Priority.LOW,
    "박물관" to Place.Priority.LOW,
    "법학관" to Place.Priority.MIDDLE,
    "상허기념도서관" to Place.Priority.HIGH,
    "의생명과학연구관" to Place.Priority.MIDDLE,
    "생명과학관" to Place.Priority.MIDDLE,
    "동물생명과학관" to Place.Priority.MIDDLE,
    "입학정보관" to Place.Priority.LOW,
    "산학협동관" to Place.Priority.HIGH,
    "수의학관" to Place.Priority.MIDDLE,
    "새천년관" to Place.Priority.HIGH,
    "건축관" to Place.Priority.MIDDLE,
    "해봉부동산학관" to Place.Priority.MIDDLE,
    "인문학관" to Place.Priority.MIDDLE,
    "학생회관" to Place.Priority.HIGH,
    "공학관" to Place.Priority.HIGH,
    "신공학관" to Place.Priority.MIDDLE,
    "과학관" to Place.Priority.MIDDLE,
    "창의관" to Place.Priority.MIDDLE,
    "KU기술혁신관" to Place.Priority.LOW,
    "쿨하우스" to Place.Priority.HIGH,
    "실내체육관" to Place.Priority.LOW,
    "일우헌" to Place.Priority.LOW,
    "중장비실험동" to Place.Priority.LOW,
    "공예관" to Place.Priority.LOW,
    "제2학생회관" to Place.Priority.MIDDLE,
    "생명과학관 부속동" to Place.Priority.LOW,
    "동물병원 별관" to Place.Priority.LOW,
    "교육연수원" to Place.Priority.LOW,
)

internal fun String.toCampusMapBuildingPriority(): Place.Priority = when {
    this == "쿨하우스" || startsWith("쿨하우스 ") -> Place.Priority.HIGH
    else -> CAMPUS_MAP_BUILDING_PRIORITY_BY_NAME[this] ?: Place.Priority.LOW
}
