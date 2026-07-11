package com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import kotlin.math.roundToInt

private val CampusMapSheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
private val CampusMapSheetHandleBottomSpacing = 8.dp
private val SheetAnimationEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
private const val SheetAnimationDurationMillis = 300
private const val SheetPositionalThresholdFraction = 0.25f

private enum class CampusMapSheetAnchor {
    Hidden,
    Collapsed,
    Expanded,
}

@Composable
internal fun CampusMapDraggableBottomSheet(
    isExpanded: Boolean,
    collapsedHeight: Dp,
    onExpandedChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (scrollModifier: Modifier) -> Unit,
) {
    val density = LocalDensity.current
    val targetAnchor = if (isExpanded) {
        CampusMapSheetAnchor.Expanded
    } else {
        CampusMapSheetAnchor.Collapsed
    }
    val collapsedHeightPx = with(density) { collapsedHeight.toPx() }
    val anchoredDraggableState = remember {
        AnchoredDraggableState(initialValue = targetAnchor)
    }
    val sheetAnimationSpec = remember {
        tween<Float>(
            durationMillis = SheetAnimationDurationMillis,
            easing = SheetAnimationEasing,
        )
    }
    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = anchoredDraggableState,
        positionalThreshold = { distance -> distance * SheetPositionalThresholdFraction },
        animationSpec = sheetAnimationSpec,
    )
    val nestedScrollConnection = remember(anchoredDraggableState, flingBehavior) {
        anchoredSheetNestedScrollConnection(
            state = anchoredDraggableState,
            flingBehavior = flingBehavior,
        )
    }

    LaunchedEffect(targetAnchor) {
        anchoredDraggableState.animateTo(
            targetValue = targetAnchor,
            animationSpec = sheetAnimationSpec,
        )
    }

    val currentIsExpanded by rememberUpdatedState(isExpanded)
    LaunchedEffect(anchoredDraggableState.settledValue) {
        when (anchoredDraggableState.settledValue) {
            CampusMapSheetAnchor.Expanded -> if (!currentIsExpanded) {
                onExpandedChange(true)
            }

            CampusMapSheetAnchor.Collapsed -> if (currentIsExpanded) {
                onExpandedChange(false)
            }

            CampusMapSheetAnchor.Hidden -> onDismiss()
        }
    }

    val scrollState = rememberScrollState()
    val scrollModifier = if (isExpanded) {
        Modifier
            .verticalScroll(scrollState)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { hostSize ->
                val positions = calculateCampusMapSheetAnchorPositions(
                    hostHeightPx = hostSize.height.toFloat(),
                    collapsedHeightPx = collapsedHeightPx,
                )
                anchoredDraggableState.updateAnchors(
                    newAnchors = DraggableAnchors {
                        CampusMapSheetAnchor.Expanded at positions.expanded
                        CampusMapSheetAnchor.Collapsed at positions.collapsed
                        CampusMapSheetAnchor.Hidden at positions.hidden
                    },
                    newTarget = targetAnchor,
                )
            },
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = 0,
                        y = anchoredDraggableState.requireOffset().roundToInt(),
                    )
                }
                .clip(CampusMapSheetShape)
                .background(KuringTheme.colors.background)
                .nestedScroll(nestedScrollConnection)
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Vertical,
                    flingBehavior = flingBehavior,
                ),
        ) {
            DragHandle()
            Spacer(modifier = Modifier.height(CampusMapSheetHandleBottomSpacing))

            content(scrollModifier)
        }
    }
}

internal data class CampusMapSheetAnchorPositions(
    val expanded: Float,
    val collapsed: Float,
    val hidden: Float,
)

internal fun calculateCampusMapSheetAnchorPositions(
    hostHeightPx: Float,
    collapsedHeightPx: Float,
): CampusMapSheetAnchorPositions {
    val height = hostHeightPx.coerceAtLeast(0f)
    val collapsedHeight = collapsedHeightPx.coerceIn(0f, height)

    return CampusMapSheetAnchorPositions(
        expanded = 0f,
        collapsed = height - collapsedHeight,
        hidden = height,
    )
}

@LightAndDarkPreview
@Composable
private fun CampusMapDraggableBottomSheetPreview() {
    KuringTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE9EEF2)),
        ) {
            CampusMapDraggableBottomSheet(
                isExpanded = false,
                collapsedHeight = 240.dp,
                onExpandedChange = {},
                onDismiss = {},
            ) { scrollModifier ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(scrollModifier),
                ) {
                    Text(
                        text = "Bottom sheet content",
                        style = KuringTheme.typography.body2,
                        color = KuringTheme.colors.textBody,
                    )
                }
            }
        }
    }
}
