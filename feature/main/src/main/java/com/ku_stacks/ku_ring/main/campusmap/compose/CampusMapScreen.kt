package com.ku_stacks.ku_ring.main.campusmap.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.campusmap.CampusMapUiState
import com.ku_stacks.ku_ring.main.campusmap.CampusMapViewModel
import com.ku_stacks.ku_ring.main.campusmap.compose.component.LibrarySeatFab
import com.ku_stacks.ku_ring.main.campusmap.compose.component.NaverMapSection
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@Composable
fun CampusMapScreen(
    onLibrarySeatFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CampusMapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CampusMapScreen(
        uiState = uiState,
        modifier = modifier,
        onMapPinClick = viewModel::updateFocusedPlace,
        onLibrarySeatFabClick = onLibrarySeatFabClick,
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun CampusMapScreen(
    uiState: CampusMapUiState,
    onMapPinClick: (Place) -> Unit,
    onLibrarySeatFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = {
            LibrarySeatFab(
                onClick = onLibrarySeatFabClick,
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        NaverMapSection(
            campusPlaces = uiState.campusPlaces,
            focusedPlace = uiState.focusedPlace,
            onMapPinClick = onMapPinClick,
        )
    }
}
