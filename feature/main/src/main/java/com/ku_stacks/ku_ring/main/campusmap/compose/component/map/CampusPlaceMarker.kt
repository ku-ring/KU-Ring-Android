package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.Place.Priority
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.rememberUpdatedMarkerState

private const val ZOOM_LEVEL_PRIORITY_HIGH = 0.0
private const val ZOOM_LEVEL_PRIORITY_MID = 14.8
private const val ZOOM_LEVEL_PRIORITY_LOW = 15.1
private const val FOCUSED_MARKER_Z_INDEX = 100

@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun CampusPlaceMarker(
    place: Place,
    isFocused: Boolean,
    selectedCategory: CampusMapCategory?,
    onClick: () -> Unit,
) {
    val position = LatLng(place.latitude, place.longitude)
    val markerState = rememberUpdatedMarkerState(position)
    val shouldForceVisible = isFocused || selectedCategory != null
    val iconRes = selectedCategory?.iconRes ?: R.drawable.ic_campus_map_icon_building

    val iconWidth = if (isFocused) 40.dp else 26.dp
    val iconHeight = if (isFocused) 48.dp else 26.dp
    val priority = if (shouldForceVisible) Priority.HIGH else place.priority
    val minZoom = when (priority) {
        Priority.HIGH -> ZOOM_LEVEL_PRIORITY_HIGH
        Priority.MIDDLE -> ZOOM_LEVEL_PRIORITY_MID
        Priority.LOW -> ZOOM_LEVEL_PRIORITY_LOW
    }
    MarkerComposable(
        place.id,
        isFocused,
        iconRes,
        state = markerState,
        width = iconWidth,
        height = iconHeight,
        anchor = Offset(0.5f, 1f),
        captionText = place.name,
        captionTextSize = 12.sp,
        captionColor = KuringTheme.colors.mainPrimary,
        captionHaloColor = KuringTheme.colors.background,
        captionOffset = 2.dp,
        zIndex = if (isFocused) FOCUSED_MARKER_Z_INDEX else 0,
        isHideCollidedSymbols = true,
        isHideCollidedCaptions = true,
        isForceShowIcon = shouldForceVisible,
        minZoom = minZoom,
        onClick = {
            onClick()
            // true를 반환하면 지도 클릭 이벤트로 전파되지 않습니다.
            true
        },
    ) {
        CampusMapMarkerIcon(
            iconRes = iconRes,
            isFocused = isFocused,
        )
    }
}

@Composable
private fun CampusMapMarkerIcon(
    @DrawableRes iconRes: Int,
    isFocused: Boolean,
) {
    if (isFocused) {
        SelectedMarkerIcon(iconRes = iconRes)
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(26.dp)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(KuringTheme.colors.mainPrimary)
                .border(
                    width = 1.dp,
                    color = KuringTheme.colors.background,
                    shape = CircleShape,
                ),
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = KuringTheme.colors.background,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun SelectedMarkerIcon(
    @DrawableRes iconRes: Int,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.size(width = 40.dp, height = 48.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_campus_map_marker_selected),
            contentDescription = null,
            modifier = Modifier.size(width = 40.dp, height = 48.dp),
        )

        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
                .size(20.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun CampusPlaceMarkerIconPreview() {
    KuringTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xFFE9EEF2))
                .padding(16.dp),
        ) {
            CampusMapMarkerIcon(
                iconRes = R.drawable.ic_campus_map_icon_building,
                isFocused = false,
            )
            CampusMapMarkerIcon(
                iconRes = CampusMapCategory.PRINT.iconRes,
                isFocused = true,
            )
        }
    }
}
