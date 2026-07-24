package com.ku_stacks.ku_ring.main.campusmap.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class CampusMapPlacePreviewParameterProvider : PreviewParameterProvider<Place> {
    override val values: Sequence<Place>
        get() = sequenceOf(CampusMapPreviewSample.place)
}

class CampusMapPlacesPreviewParameterProvider : PreviewParameterProvider<ImmutableList<Place>> {
    override val values: Sequence<ImmutableList<Place>>
        get() = sequenceOf(
            persistentListOf(
                CampusMapPreviewSample.place,
                CampusMapPreviewSample.place.copy(
                    id = "preview-building-2",
                    name = "학생회관",
                    category = "복지시설",
                    address = "서울 광진구 능동로 120 학생회관",
                ),
                CampusMapPreviewSample.place.copy(
                    id = "preview-building-3",
                    name = "새천년관",
                    category = "강의동",
                    address = "서울 광진구 능동로 120 새천년관",
                ),
            )
        )
}

private object CampusMapPreviewSample {
    val place = Place(
        id = "preview-building",
        name = "상허기념도서관",
        category = "건물",
        address = "서울 광진구 능동로 120",
        latitude = 37.541,
        longitude = 127.078,
        priority = Place.Priority.HIGH,
        imageUrl = null,
        operationHours = PlaceOperationHours(
            current = "22:00에 종료",
            semester = "09:00-22:00",
            vacation = "10:00-17:00",
        ),
        facilities = listOf(
            PlaceFacility(
                name = "프린터",
                category = "프린터",
                location = "1층 로비",
                operationHours = PlaceOperationHours(
                    current = "22:00에 종료",
                    semester = "09:00-22:00",
                    vacation = "10:00-17:00",
                ),
            ),
            PlaceFacility(
                name = "카페",
                category = "카페",
                location = "지하 1층",
                operationHours = PlaceOperationHours(
                    current = "20:00에 종료",
                    semester = "08:30-20:00",
                    vacation = "10:00-16:00",
                ),
            ),
            PlaceFacility(
                name = "K-CUBE",
                category = "k-cube",
                location = "2층",
                operationHours = PlaceOperationHours(
                    current = "18:00에 종료",
                    semester = "09:00-18:00",
                    vacation = "휴무",
                ),
            ),
        ),
    )
}
