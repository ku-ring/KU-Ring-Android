package com.ku_stacks.ku_ring.main.campusmap.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.Place.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

private const val ZOOM_LEVEL_PRIORITY_HIGH = 0.0
private const val ZOOM_LEVEL_PRIORITY_MID = 14.8
private const val ZOOM_LEVEL_PRIORITY_LOW = 15.1

@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun CampusPlaceMarker(
    place: Place,
    isFocused: Boolean,
    zIndex: Int,
    onClick: (Marker) -> Unit,
) {
    val position = LatLng(place.latitude, place.longitude)
    val markerState = MarkerState(position)

    val iconSizeDp = if (isFocused) 40.dp else 30.dp
    val priority = if (isFocused) Priority.HIGH else place.priority
    val minZoom = when (priority) {
        Priority.HIGH -> ZOOM_LEVEL_PRIORITY_HIGH
        Priority.MIDDLE -> ZOOM_LEVEL_PRIORITY_MID
        Priority.LOW -> ZOOM_LEVEL_PRIORITY_LOW
    }

    Marker(
        state = markerState,
        icon = OverlayImage.fromResource(R.drawable.ic_map_pin_fill_v2),
        iconTintColor = KuringTheme.colors.mainPrimary,
        width = iconSizeDp,
        height = iconSizeDp,
        captionText = place.name,//if (isFocused) place.name else null,
        zIndex = zIndex,
        isHideCollidedSymbols = true,
        isHideCollidedCaptions = true,
        minZoom = minZoom,
        onClick = {
            onClick(it)
            // true를 반환하면 지도 클릭 이벤트로 전파되지 않습니다.
            true
        },
    )
}
