package com.ku_stacks.ku_ring.main.campusmap.compose

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.ku_stacks.ku_ring.compose.locals.LocalPreferences
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.campusmap.CampusMapUiState
import com.ku_stacks.ku_ring.main.campusmap.CampusMapViewModel
import com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet.CampusMapSearchResultSheetContent
import com.ku_stacks.ku_ring.main.campusmap.compose.component.map.CompassFab
import com.ku_stacks.ku_ring.main.campusmap.compose.component.map.CurrentLocationFab
import com.ku_stacks.ku_ring.main.campusmap.compose.component.map.LibrarySeatFab
import com.ku_stacks.ku_ring.main.campusmap.compose.component.map.NaverMapSection
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapSearchResult
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategoryItem
import com.ku_stacks.ku_ring.util.checkHasLocationPermission
import com.ku_stacks.ku_ring.util.isLocationPermissionPermanentlyDenied
import com.ku_stacks.ku_ring.util.openAppSettings
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdateParams
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.collections.immutable.ImmutableList

private val BottomFabMargin = 16.dp
private const val INITIAL_LATITUDE = 37.542366
private const val INITIAL_LONGITUDE = 127.076846
private const val INITIAL_ZOOM_LEVEL = 14.2

@OptIn(ExperimentalNaverMapApi::class)
@Composable
internal fun CampusMapScreen(
    onLibrarySeatFabClick: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onSearchResultSheetContentChange: (CampusMapSearchResultSheetContent?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CampusMapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val preferences = LocalPreferences.current
    val snackbarHostState = remember { SnackbarHostState() }
    val loadErrorMessage = stringResource(R.string.campus_map_load_error)
    val retryLabel = stringResource(R.string.campus_map_retry)
    val failedRequest = uiState.mapFailedRequest
    var hasLocationPermission by remember { mutableStateOf(context.checkHasLocationPermission()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            LatLng(INITIAL_LATITUDE, INITIAL_LONGITUDE),
            INITIAL_ZOOM_LEVEL,
        )
    }
    val fusedLocationClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val moveToCurrentLocation = {
        requestCurrentLocation(fusedLocationClient) { latitude, longitude ->
            cameraPositionState.move(
                update = CameraUpdate
                    .scrollTo(LatLng(latitude, longitude))
                    .animate(CameraAnimation.Fly),
            )
        }
    }

    LifecycleResumeEffect(Unit) {
        hasLocationPermission = context.checkHasLocationPermission()
        onPauseOrDispose {}
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        hasLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) {
            moveToCurrentLocation()
        }
    }

    LaunchedEffect(failedRequest) {
        if (failedRequest == null) {
            snackbarHostState.currentSnackbarData?.dismiss()
            return@LaunchedEffect
        }

        if (
            snackbarHostState.showSnackbar(
                message = loadErrorMessage,
                actionLabel = retryLabel,
                duration = SnackbarDuration.Indefinite,
            ) == SnackbarResult.ActionPerformed
        ) {
            viewModel.retryFailedRequest(failedRequest)
        }
    }

    CampusMapScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        onMapPinClick = viewModel::focusMapPlace,
        onMapClick = viewModel::clearFocusedPlace,
        onPlaceDetailClose = viewModel::clearFocusedPlace,
        onSearchResultSheetContentChange = onSearchResultSheetContentChange,
        onCategoryClick = viewModel::updateSelectedCategory,
        onSearchClick = {
            viewModel.prepareSearchInput()
            onNavigateToSearch()
        },
        onSearchResultClick = viewModel::focusSearchResult,
        onActiveSelectionClear = viewModel::clearActiveSelection,
        cameraPositionState = cameraPositionState,
        hasLocationPermission = hasLocationPermission,
        onCurrentLocationClick = {
            when {
                context.checkHasLocationPermission() -> moveToCurrentLocation()
                activity?.isLocationPermissionPermanentlyDenied(
                    hasRequestedPermission = preferences.hasRequestedCampusMapLocationPermission,
                ) == true -> viewModel.showLocationPermissionDialog()

                else -> {
                    preferences.hasRequestedCampusMapLocationPermission = true
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }
            }
        },
        onLibrarySeatFabClick = onLibrarySeatFabClick,
    )

    if (uiState.isLocationPermissionDialogVisible) {
        KuringAlertDialog(
            text = stringResource(R.string.campus_map_location_permission_dialog_text),
            confirmText = stringResource(R.string.notification_permission_dialog_confirm),
            onConfirm = {
                viewModel.hideLocationPermissionDialog()
                context.openAppSettings()
            },
            onCancel = viewModel::hideLocationPermissionDialog,
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun CampusMapScreen(
    uiState: CampusMapUiState,
    snackbarHostState: SnackbarHostState,
    onMapPinClick: (Place) -> Unit,
    onMapClick: () -> Unit,
    onPlaceDetailClose: () -> Unit,
    onSearchResultSheetContentChange: (CampusMapSearchResultSheetContent?) -> Unit,
    onCategoryClick: (CampusMapCategory) -> Unit,
    onSearchClick: () -> Unit,
    onSearchResultClick: (CampusMapSearchResult) -> Unit,
    onActiveSelectionClear: () -> Unit,
    cameraPositionState: CameraPositionState,
    hasLocationPermission: Boolean,
    onCurrentLocationClick: () -> Unit,
    onLibrarySeatFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(
        enabled = uiState.mapFocusedPlace != null || uiState.showSearchResultSheet,
    ) {
        if (uiState.mapFocusedPlace != null) {
            onPlaceDetailClose()
        } else {
            onActiveSelectionClear()
        }
    }

    CampusMapSearchResultSheetEffect(
        uiState = uiState,
        onResultClick = onSearchResultClick,
        onDismiss = onActiveSelectionClear,
        onContentChange = onSearchResultSheetContentChange,
    )

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        NaverMapSection(
            campusPlaces = uiState.visiblePlaces,
            focusedPlace = uiState.mapFocusedPlace,
            selectedCategories = uiState.selectedCategories,
            isSearchResultVisible = uiState.showSearchResultSheet &&
                !uiState.submittedSearchQuery.isNullOrBlank(),
            cameraPositionState = cameraPositionState,
            onMapPinClick = onMapPinClick,
            onMapClick = onMapClick,
            modifier = Modifier.fillMaxSize(),
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = BottomFabMargin,
                ),
        ) {
            LibrarySeatFab(onClick = onLibrarySeatFabClick)

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                CompassFab(
                    bearing = cameraPositionState.position.bearing,
                    onClick = cameraPositionState::resetBearing,
                )

                CurrentLocationFab(
                    hasLocationPermission = hasLocationPermission,
                    onClick = onCurrentLocationClick,
                )
            }
        }

        CampusMapTopControls(
            categories = uiState.categories,
            selectedCategories = uiState.selectedCategories,
            activeSearchText = uiState.activeSearchText,
            showCategoryChips = uiState.showCategoryChips,
            onSearchClick = onSearchClick,
            onActiveSearchClear = onActiveSelectionClear,
            onCategoryClick = onCategoryClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
        )

        if (uiState.isMapLoading) {
            CircularProgressIndicator(
                color = KuringTheme.colors.mainPrimary,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Composable
private fun CampusMapTopControls(
    categories: ImmutableList<CampusMapCategoryItem>,
    selectedCategories: ImmutableList<CampusMapCategory>,
    activeSearchText: String?,
    showCategoryChips: Boolean,
    onSearchClick: () -> Unit,
    onActiveSearchClear: () -> Unit,
    onCategoryClick: (CampusMapCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (activeSearchText != null) {
                        KuringTheme.colors.background
                    } else {
                        Color.Transparent
                    },
                )
                .statusBarsPadding()
                .padding(top = if (activeSearchText == null) 8.dp else 0.dp),
        ) {
            if (activeSearchText.isNullOrBlank()) {
                CampusMapSearchField(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            } else {
                CampusMapSelectedSearchField(
                    text = activeSearchText,
                    onClick = onSearchClick,
                    onClear = onActiveSearchClear,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (showCategoryChips && categories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            CampusMapCategoryChips(
                categories = categories,
                selectedCategories = selectedCategories,
                onCategoryClick = onCategoryClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CampusMapSearchField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .height(40.dp)
            .shadow(elevation = 4.dp, shape = shape)
            .clip(shape)
            .background(KuringTheme.colors.background)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            )
            .padding(start = 16.dp, end = 12.dp),
    ) {
        Text(
            text = stringResource(id = R.string.campus_map_search_placeholder),
            style = KuringTheme.typography.body2,
            color = KuringTheme.colors.textCaption1,
            modifier = Modifier.weight(1f),
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_search_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun CampusMapSelectedSearchField(
    text: String,
    onClick: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(KuringTheme.colors.background)
            .padding(start = 4.dp, end = 20.dp, top = 6.dp, bottom = 6.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clickable(
                    role = Role.Button,
                    onClick = onClear,
                ),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_left_v2),
                contentDescription = stringResource(id = R.string.campus_map_search_navigate_up),
                tint = KuringTheme.colors.gray600,
                modifier = Modifier.size(24.dp),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(KuringTheme.colors.gray100)
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(start = 16.dp, end = 12.dp),
        ) {
            Text(
                text = text,
                style = KuringTheme.typography.body2.copy(fontWeight = FontWeight.Medium),
                color = KuringTheme.colors.textTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            SelectedSearchClearIcon(onClick = onClear)
        }
    }
}

@Composable
private fun SelectedSearchClearIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(KuringTheme.colors.gray300)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_x_v2),
            contentDescription = stringResource(id = R.string.campus_map_clear_search_query),
            tint = KuringTheme.colors.background,
            modifier = Modifier.size(14.dp),
        )
    }
}

@Composable
private fun CampusMapCategoryChips(
    categories: ImmutableList<CampusMapCategoryItem>,
    selectedCategories: ImmutableList<CampusMapCategory>,
    onCategoryClick: (CampusMapCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        categories.forEach { item ->
            CampusMapCategoryChip(
                item = item,
                isSelected = item.category in selectedCategories,
                onClick = { onCategoryClick(item.category) },
            )
        }
    }
}

@Composable
private fun CampusMapCategoryChip(
    item: CampusMapCategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (isSelected) {
        KuringTheme.colors.mainPrimary
    } else {
        KuringTheme.colors.textBody
    }
    val shape = RoundedCornerShape(999.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.defaultMinSize(minHeight = 44.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .heightIn(min = 34.dp)
                .shadow(elevation = 4.dp, shape = shape)
                .clip(shape)
                .background(KuringTheme.colors.background)
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 1.4.dp,
                            color = KuringTheme.colors.mainPrimary,
                            shape = shape,
                        )
                    } else {
                        Modifier
                    },
                )
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = item.category.iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )

            Text(
                text = item.label,
                style = KuringTheme.typography.caption1_1,
                color = contentColor,
            )
        }
    }
}

@Composable
private fun CampusMapSearchResultSheetEffect(
    uiState: CampusMapUiState,
    onResultClick: (CampusMapSearchResult) -> Unit,
    onDismiss: () -> Unit,
    onContentChange: (CampusMapSearchResultSheetContent?) -> Unit,
) {
    val currentOnResultClick by rememberUpdatedState(onResultClick)
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val currentOnContentChange by rememberUpdatedState(onContentChange)
    val results = uiState.searchResults

    DisposableEffect(uiState.showSearchResultSheet, results) {
        currentOnContentChange(
            if (uiState.showSearchResultSheet) {
                CampusMapSearchResultSheetContent(
                    results = results,
                    onResultClick = currentOnResultClick,
                    onDismiss = { currentOnDismiss() },
                )
            } else {
                null
            },
        )
        onDispose {
            currentOnContentChange(null)
        }
    }
}

@SuppressLint("MissingPermission")
private fun requestCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationAvailable: (latitude: Double, longitude: Double) -> Unit,
) {
    fusedLocationClient
        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            location?.let {
                onLocationAvailable(it.latitude, it.longitude)
            }
        }
}

@OptIn(ExperimentalNaverMapApi::class)
private fun CameraPositionState.resetBearing() {
    move(
        update = CameraUpdate
            .withParams(
                CameraUpdateParams()
                    .rotateTo(0.0),
            )
            .animate(CameraAnimation.Easing),
    )
}
