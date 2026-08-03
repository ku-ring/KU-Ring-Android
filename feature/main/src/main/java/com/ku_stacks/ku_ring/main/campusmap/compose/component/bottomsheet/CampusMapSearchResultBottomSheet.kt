package com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.campusmap.model.CampusMapSearchResult
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList

internal data class CampusMapSearchResultSheetContent(
    val results: ImmutableList<CampusMapSearchResult>,
    val onResultClick: (CampusMapSearchResult) -> Unit,
    val onDismiss: () -> Unit,
)

@Composable
internal fun CampusMapSearchResultSheetHost(
    content: CampusMapSearchResultSheetContent,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .clipToBounds(),
    ) {
        CampusMapSearchResultBottomSheet(
            results = content.results,
            onResultClick = content.onResultClick,
            onDismiss = content.onDismiss,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val SearchResultSheetBottomPadding = 96.dp
private val SearchResultSheetAnimationEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
private const val SearchResultSheetAnimationDurationMillis = 300
private const val SearchResultSheetPositionalThresholdFraction = 0.25f

private enum class SearchResultSheetAnchor {
    Visible,
    Hidden,
}

@Composable
internal fun CampusMapSearchResultBottomSheet(
    results: ImmutableList<CampusMapSearchResult>,
    onResultClick: (CampusMapSearchResult) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    val anchoredDraggableState = remember {
        AnchoredDraggableState(initialValue = SearchResultSheetAnchor.Visible)
    }
    val animationSpec = remember {
        tween<Float>(
            durationMillis = SearchResultSheetAnimationDurationMillis,
            easing = SearchResultSheetAnimationEasing,
        )
    }
    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = anchoredDraggableState,
        positionalThreshold = { distance ->
            distance * SearchResultSheetPositionalThresholdFraction
        },
        animationSpec = animationSpec,
    )
    val nestedScrollConnection = remember(anchoredDraggableState, flingBehavior) {
        anchoredSheetNestedScrollConnection(
            state = anchoredDraggableState,
            flingBehavior = flingBehavior,
        )
    }
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    LaunchedEffect(results) {
        scrollState.scrollTo(0)
    }

    LaunchedEffect(anchoredDraggableState.settledValue) {
        if (anchoredDraggableState.settledValue == SearchResultSheetAnchor.Hidden) {
            currentOnDismiss()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(CampusMapCollapsedSheetHeight)
            .onSizeChanged { sheetSize ->
                val positions = calculateSearchResultSheetAnchorPositions(
                    sheetHeightPx = sheetSize.height.toFloat(),
                )
                anchoredDraggableState.updateAnchors(
                    newAnchors = DraggableAnchors {
                        SearchResultSheetAnchor.Visible at positions.visible
                        SearchResultSheetAnchor.Hidden at positions.hidden
                    },
                    newTarget = anchoredDraggableState.targetValue,
                )
            }
            .offset {
                IntOffset(
                    x = 0,
                    y = anchoredDraggableState.offset
                        .takeUnless(Float::isNaN)
                        ?.roundToInt()
                        ?: 0,
                )
            }
            .clip(shape)
            .background(KuringTheme.colors.background)
            .nestedScroll(nestedScrollConnection)
            .anchoredDraggable(
                state = anchoredDraggableState,
                orientation = Orientation.Vertical,
                flingBehavior = flingBehavior,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            DragHandle()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = SearchResultSheetBottomPadding,
                    ),
            ) {
                if (results.isEmpty()) {
                    EmptySearchResultContent(modifier = Modifier.fillMaxWidth())
                } else {
                    results.forEachIndexed { index, result ->
                        SearchResultListItem(
                            title = result.title,
                            category = result.category,
                            location = result.location,
                            operationHours = result.operationHours,
                            imageUrl = result.imageUrl,
                            onClick = { onResultClick(result) },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        if (index != results.lastIndex) {
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(color = KuringTheme.colors.borderline)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

internal data class SearchResultSheetAnchorPositions(
    val visible: Float,
    val hidden: Float,
)

internal fun calculateSearchResultSheetAnchorPositions(
    sheetHeightPx: Float,
): SearchResultSheetAnchorPositions = SearchResultSheetAnchorPositions(
    visible = 0f,
    hidden = sheetHeightPx.coerceAtLeast(0f),
)

@Composable
internal fun SearchResultListItem(
    title: String,
    category: String,
    location: String,
    operationHours: String?,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .background(KuringTheme.colors.background)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            )
            .padding(vertical = 10.dp),
    ) {
        imageUrl
            ?.takeIf { it.isNotBlank() }
            ?.let { availableImageUrl ->
                SearchResultThumbnail(
                    imageUrl = availableImageUrl,
                    modifier = Modifier.size(50.dp),
                )
            }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    style = KuringTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold),
                    color = KuringTheme.colors.textBody,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )

                Text(
                    text = category,
                    style = KuringTheme.typography.caption1,
                    color = KuringTheme.colors.textCaption1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = location,
                    style = KuringTheme.typography.caption1,
                    color = KuringTheme.colors.textBody,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )

                operationHours
                    ?.takeIf { it.isNotBlank() }
                    ?.let { availableOperationHours ->
                        Box(
                            modifier = Modifier
                                .size(width = 1.dp, height = 12.dp)
                                .background(KuringTheme.colors.borderline),
                        )

                        Text(
                            text = availableOperationHours,
                            style = KuringTheme.typography.caption1,
                            color = KuringTheme.colors.textBody,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
            }
        }
    }
}

@Composable
private fun SearchResultThumbnail(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(KuringTheme.colors.gray200),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SearchResultListItemPreview() {
    KuringTheme {
        SearchResultListItem(
            title = "상허기념도서관",
            category = "프린터",
            location = "1층 로비",
            operationHours = "22:00에 종료",
            imageUrl = null,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
    }
}

@Composable
private fun EmptySearchResultContent(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 8.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_campus_map_empty_search),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(150.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.campus_map_empty_filtered_result),
            style = KuringTheme.typography.body2,
            color = KuringTheme.colors.textCaption1,
        )
    }
}
