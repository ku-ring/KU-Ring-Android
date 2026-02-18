package com.ku_stacks.ku_ring.club.detail

import android.view.Gravity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ku_stacks.ku_ring.club.R
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubLocation
import com.ku_stacks.ku_ring.domain.RecruitmentStatus
import com.ku_stacks.ku_ring.domain.calculateDDay
import com.ku_stacks.ku_ring.ui.club.ClubDeadlineTag
import com.ku_stacks.ku_ring.ui.club.ClubTag
import com.ku_stacks.ku_ring.util.showToast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage

@Composable
fun ClubDetailScreen(
    onBack: () -> Unit,
    onMoveToRecruitmentLink: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClubDetailViewModel = hiltViewModel(),
) {
    val clubUiState by viewModel.clubUiState.collectAsStateWithLifecycle()
    when (clubUiState) {
        ClubDetailUiState.Loading -> {
            ClubLoadingScreen()
        }

        is ClubDetailUiState.Success -> {
            ClubDetailScreen(
                club = (clubUiState as ClubDetailUiState.Success).club,
                onBack = onBack,
                onSubscribeButtonClick = viewModel::updateSubscription,
                onMoveToRecruitmentLink = onMoveToRecruitmentLink,
                modifier = modifier,
            )
        }

        is ClubDetailUiState.Error -> {
            ClubErrorScreen(
                errorMessage = (clubUiState as ClubDetailUiState.Error).message,
                onRetry = viewModel::loadClub,
                modifier = modifier,
            )
        }
    }

    val lifecycleOwner = rememberLifecycleOwner()
    val context = LocalContext.current
    LaunchedEffect(viewModel.toastByResource, lifecycleOwner) {
        viewModel.toastByResource.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect(context::showToast)
    }
}

@Composable
private fun ClubDetailScreen(
    club: Club,
    onBack: () -> Unit,
    onSubscribeButtonClick: (Boolean) -> Unit,
    onMoveToRecruitmentLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterTitleTopBar(
                title = stringResource(R.string.club_detail_title),
                modifier = Modifier.fillMaxWidth(),
                navigation = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_v2),
                        contentDescription = stringResource(id = R.string.club_detail_back_description),
                        modifier = Modifier.rotate(180f),
                    )
                },
                onNavigationClick = onBack,
                action = {
                    ClubSubscribeButton(
                        subscribeCount = club.subscribeCount,
                        isSubscribed = club.isSubscribed,
                        onClick = onSubscribeButtonClick,
                    )
                },
            )
        },
        bottomBar = {
            val enabled = club.recruitment?.recruitmentStatus == RecruitmentStatus.RECRUITING
            KuringCallToAction(
                text = if (enabled) {
                    stringResource(R.string.club_detail_cta_recruiting)
                } else {
                    stringResource(R.string.club_detail_cta_not_recruiting)
                },
                onClick = {
                    club.recruitment?.applyLink?.let(onMoveToRecruitmentLink)
                },
                enabled = enabled,
                blur = true,
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxWidth(),
            )
        },
        containerColor = KuringTheme.colors.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            ClubInfo(club)
            Spacer(modifier = Modifier.height(24.dp))
            ClubLinks(club)
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = KuringTheme.colors.borderline,
            )
            if (club.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                ClubInfoLong(club.description)
            }
            club.applyQualification?.let { qualification ->
                Spacer(modifier = Modifier.height(24.dp))
                ClubRecruitmentQualification(qualification)
            }
            Spacer(modifier = Modifier.height(24.dp))
            club.descriptionImageUrl?.let { imageLinks ->
                ClubDescriptionImages(
                    imageLinks = imageLinks,
                )
            }
            ClubRequestUpdate(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun ClubSubscribeButton(
    subscribeCount: Int,
    isSubscribed: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = { onClick(!isSubscribed) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (subscribeCount > 99) "99+" else subscribeCount.toString(),
            style = KuringTheme.typography.caption1_1,
            color = KuringTheme.colors.textCaption1,
        )
        AnimatedContent(
            targetState = isSubscribed,
            label = "subscribe icon animation",
        ) { targetState ->
            val iconRes = if (targetState) R.drawable.ic_star_fill_v2 else R.drawable.ic_star_v2
            Icon(
                imageVector = ImageVector.vectorResource(id = iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun ClubInfo(
    club: Club,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ClubTags(club)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = club.name,
            style = KuringTheme.typography.title1,
            color = KuringTheme.colors.textTitle,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = club.summary,
            style = KuringTheme.typography.body2,
            color = KuringTheme.colors.textBody,
        )
    }
}

@Composable
private fun ClubLinks(
    club: Club,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        club.webUrl.map { SnsUrl.from(it) }.forEach { snsUrl ->
            SnsButton(snsUrl = snsUrl)
        }
        club.location?.let {
            ClubLocation(location = it)
        }
    }
}

@Composable
private fun SnsButton(
    snsUrl: SnsUrl,
    modifier: Modifier = Modifier,
) {
    val text = stringResource(id = snsUrl.textResId)
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = text },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(id = snsUrl.iconResId),
            contentDescription = null,
            tint = KuringTheme.colors.textBody,
        )
        Text(
            text = text,
            style = KuringTheme.typography.caption1,
            color = KuringTheme.colors.textBody,
        )
    }
}

@Composable
private fun ClubLocation(
    location: ClubLocation,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ClubLocationButton(location)
        if (location.latitude != null && location.longitude != null) {
            ClubLocationMap(location)
        }
    }
}

@Composable
private fun ClubLocationButton(
    location: ClubLocation,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = location.fullLocation },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_map_pin_v2),
            contentDescription = null,
            tint = KuringTheme.colors.textBody,
        )
        Text(
            text = location.fullLocation,
            style = KuringTheme.typography.caption1,
            color = KuringTheme.colors.textBody,
        )
    }
}

// TODO: Map 영역 공통화하기 (see CampusMapScreen)
@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun ClubLocationMap(
    location: ClubLocation,
    modifier: Modifier = Modifier,
) {
    val (latitude, longitude) = location.latitude!! to location.longitude!!

    val zoomLevel = 15.0
    val mapUiSettings = MapUiSettings(
        isTiltGesturesEnabled = false,
        isScaleBarEnabled = false,
        isCompassEnabled = false,
        isZoomControlEnabled = false,
        zoomGesturesFriction = 0f,
        logoGravity = Gravity.TOP or Gravity.END,
    )
    val mapProperties = MapProperties(
        minZoom = zoomLevel,
        maxZoom = zoomLevel,
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            LatLng(latitude, longitude),
            zoomLevel,
        )
    }
    LaunchedEffect(location) {
        cameraPositionState.move(
            update = CameraUpdate.scrollTo(LatLng(latitude, longitude))
                .animate(CameraAnimation.Fly)
        )
    }

    NaverMap(
        properties = mapProperties,
        uiSettings = mapUiSettings,
        cameraPositionState = cameraPositionState,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .aspectRatio(2.0f),
    ) {
        val position = LatLng(latitude, longitude)
        val markerState = MarkerState(position)

        val iconSizeDp = 30.dp

        Marker(
            state = markerState,
            icon = OverlayImage.fromResource(R.drawable.ic_map_pin_fill_v2),
            iconTintColor = KuringTheme.colors.mainPrimary,
            width = iconSizeDp,
            height = iconSizeDp,
            captionText = location.fullLocation,
            isHideCollidedSymbols = true,
            isHideCollidedCaptions = true,
            minZoom = zoomLevel,
            onClick = { true },
        )
    }
}

@Composable
private fun ClubInfoLong(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        style = KuringTheme.typography.body2,
        color = KuringTheme.colors.textBody,
        modifier = modifier,
    )
}

@Composable
private fun ClubRecruitmentQualification(
    qualification: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.club_detail_qualification_title),
            style = KuringTheme.typography.caption1_1,
            color = KuringTheme.colors.textBody,
        )
        Text(
            text = qualification,
            style = KuringTheme.typography.body2,
            color = KuringTheme.colors.textBody,
        )
    }
}

@Composable
private fun ClubRequestUpdate(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.club_detail_request_update),
        style = KuringTheme.typography.caption1_1,
        color = KuringTheme.colors.textCaption1,
        textDecoration = TextDecoration.Underline,
        modifier = modifier,
    )
}

@Composable
private fun ClubDescriptionImages(
    imageLinks: List<String>,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier.fillMaxWidth()) {
        itemsIndexed(
            items = imageLinks,
            key = { _, url -> url },
        ) { index, url ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.club_detail_description_image, index),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun ClubTags(
    club: Club,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        club.recruitment?.let { recruitment ->
            ClubDeadlineTag(
                dDay = club.calculateDDay() ?: 0,
                recruitmentStatus = recruitment.recruitmentStatus,
            )
        }
        ClubTag(text = club.category.koreanName)
        ClubTag(text = club.division.koreanName)
    }
}

@Composable
private fun ClubLoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            color = KuringTheme.colors.mainPrimary,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun ClubErrorScreen(
    errorMessage: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val message = errorMessage ?: stringResource(R.string.club_detail_load_error)
    val description = stringResource(R.string.club_detail_load_error_description)
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onRetry)
                .padding(8.dp)
                .clearAndSetSemantics {
                    contentDescription = description
                }
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_refresh_v2),
                contentDescription = null,
                tint = KuringTheme.colors.textCaption1,
                modifier = Modifier.size(30.dp),
            )
            Text(
                text = message,
                style = KuringTheme.typography.caption1,
                color = KuringTheme.colors.textCaption1,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun ClubLoadingScreenPreview() {
    KuringTheme {
        ClubLoadingScreen(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubErrorScreenPreview() {
    KuringTheme {
        ClubErrorScreen(
            errorMessage = null,
            onRetry = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}