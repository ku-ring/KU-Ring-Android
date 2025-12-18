package com.ku_stacks.ku_ring.main.campusmap.compose.component

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
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.collections.immutable.ImmutableList

private const val INITIAL_LATITUDE = 37.542366
private const val INITIAL_LONGITUDE = 127.076846
private const val INITIAL_ZOOM_LEVEL = 14.2
private const val MIN_ZOOM_LEVEL = 5.0
private const val MAX_ZOOM_LEVEL = 21.0


@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun NaverMapSection(
    campusPlaces: ImmutableList<Place>,
    focusedPlace: Place?,
    onMapPinClick: (Place) -> Unit,
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
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            LatLng(INITIAL_LATITUDE, INITIAL_LONGITUDE),
            INITIAL_ZOOM_LEVEL,
        )
    }

    LaunchedEffect(focusedPlace) {
        focusedPlace?.let { place ->
            cameraPositionState.animateMoveTo(place)
        }
    }

    NaverMap(
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        modifier = modifier.fillMaxSize(),
    ) {

        campusPlaces.forEach { place ->
            val isFocused = place == focusedPlace
            CampusPlaceMarker(
                place = place,
                isFocused = isFocused,
                zIndex = if(isFocused) 100 else 0,
            ) {
                onMapPinClick(place)
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun CampusPlaceMarker(
    place: Place,
    isFocused: Boolean,
    zIndex: Int,
    onClick: (Marker) -> Unit,
) {
    val position = LatLng(place.latitude, place.longitude)
    val markerState = MarkerState(position)

    val iconSizeDp = if (isFocused) 40.dp else 30.dp

    Marker(
        state = markerState,
        icon = OverlayImage.fromResource(R.drawable.ic_map_pin_fill_v2),
        iconTintColor = KuringTheme.colors.mainPrimary,
        width = iconSizeDp,
        height = iconSizeDp,
        captionText = if(isFocused) place.name else null,
        zIndex = zIndex,
        isHideCollidedSymbols = true,
        onClick = {
            onClick(it)
            // true를 반환하면 지도 클릭 이벤트로 전파되지 않습니다.
            true
        },
    )
}

@OptIn(ExperimentalNaverMapApi::class)
private fun CameraPositionState.animateMoveTo(place: Place) {
    move(
        update = CameraUpdate
            .scrollTo(LatLng(place.latitude, place.longitude))
            .animate(CameraAnimation.Fly)
    )
}
