package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import kotlinx.collections.immutable.ImmutableList

private const val MIN_ZOOM_LEVEL = 13.0 // 지도를 축소할 수 있는 한계
private const val MAX_ZOOM_LEVEL = 21.0 // 지도를 확대할 수 있는 한계
private val CAMPUS_MAP_EXTENT = LatLngBounds(
    LatLng(37.5320, 127.0650),
    LatLng(37.5505, 127.0910),
)

@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun NaverMapSection(
    campusPlaces: ImmutableList<Place>,
    focusedPlace: Place?,
    selectedCategories: ImmutableList<CampusMapCategory>,
    isSearchResultVisible: Boolean,
    cameraPositionState: CameraPositionState,
    onMapPinClick: (Place) -> Unit,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = !KuringTheme.colors.isLight
    val mapUiSettings = MapUiSettings(
        isTiltGesturesEnabled = false,
        isScaleBarEnabled = false,
        isCompassEnabled = false,
        isZoomControlEnabled = false,
        logoGravity = Gravity.BOTTOM or Gravity.START,
        logoMargin = PaddingValues(
            start = 20.dp,
            bottom = 80.dp,
        ),
    )
    val mapProperties = MapProperties(
        mapType = if (isDarkTheme) MapType.Navi else MapType.Basic,
        extent = CAMPUS_MAP_EXTENT,
        minZoom = MIN_ZOOM_LEVEL,
        maxZoom = MAX_ZOOM_LEVEL,
        isNightModeEnabled = isDarkTheme,
    )

    LaunchedEffect(focusedPlace?.id) {
        focusedPlace?.let { place ->
            cameraPositionState.move(
                update = CameraUpdate
                    .scrollTo(LatLng(place.latitude, place.longitude))
                    .animate(CameraAnimation.Fly),
            )
        }
    }

    NaverMap(
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        onMapClick = { _, _ ->
            onMapClick()
        },
        modifier = modifier.fillMaxSize(),
    ) {
        campusPlaces.forEach { place ->
            val isFocused = place.id == focusedPlace?.id
            CampusPlaceMarker(
                place = place,
                isFocused = isFocused,
                selectedCategories = selectedCategories,
                isSearchResultVisible = isSearchResultVisible,
            ) {
                onMapPinClick(place)
            }
        }
    }
}
