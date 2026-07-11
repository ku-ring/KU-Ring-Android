package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import kotlinx.collections.immutable.ImmutableList

private const val MIN_ZOOM_LEVEL = 5.0           // 사용자가 설정할 수 있는 지도의 최소 확대 레벨
private const val MAX_ZOOM_LEVEL = 21.0          // 사용자가 설정할 수 있는 지도의 최대 확대 레벨

@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun NaverMapSection(
    campusPlaces: ImmutableList<Place>,
    focusedPlace: Place?,
    selectedCategory: CampusMapCategory?,
    cameraPositionState: CameraPositionState,
    onMapPinClick: (Place) -> Unit,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mapUiSettings = MapUiSettings(
        isTiltGesturesEnabled = false,
        isScaleBarEnabled = false,
        isCompassEnabled = false,
        isZoomControlEnabled = false,
        logoGravity = Gravity.TOP or Gravity.END,
        logoMargin = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding().plus(10.dp),
            end = 10.dp,
        )
    )
    val mapProperties = MapProperties(
        minZoom = MIN_ZOOM_LEVEL,
        maxZoom = MAX_ZOOM_LEVEL,
    )

    LaunchedEffect(focusedPlace) {
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
            val isFocused = place == focusedPlace
            CampusPlaceMarker(
                place = place,
                isFocused = isFocused,
                selectedCategory = selectedCategory,
            ) {
                onMapPinClick(place)
            }
        }
    }
}
