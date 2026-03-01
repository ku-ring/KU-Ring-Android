package com.ku_stacks.ku_ring.notification.compose.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.ensureLineHeight
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_swipe_to_delete_box_delete
import com.ku_stacks.ku_ring.notification.compose.preview.NotificationPreviewParameterProvider
import com.ku_stacks.ku_ring.notification.model.DaysSinceReceived
import com.ku_stacks.ku_ring.notification.model.NotificationUiModel
import kotlin.math.roundToInt

enum class DragValue { Settled, Revealed }

/**
 * 알림 내용을 보여주며, 좌로 스와이프하면 삭제 버튼이 표시되는 컴포넌트입니다.
 *
 * @param notificationUiModel 삭제 버튼이 표시될 컨텐츠
 * @param onDelete 삭제 버튼을 클릭했을 때 호출되는 콜백
 * @param onClick 컨텐츠를 클릭했을 때 호출되는 콜백
 * @param onReveal 좌로 스와이프했을 때 호출되는 콜백
 * @param modifier 컴포넌트의 레이아웃을 설정하는 데 사용되는 Modifier
 * @param isRevealed 삭제 버튼 표시 여부
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToRevealDeleteBox(
    notificationUiModel: NotificationUiModel,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onReveal: () -> Unit,
    modifier: Modifier = Modifier,
    isRevealed: Boolean = false,
) {
    val density = LocalDensity.current
    val revealWidth = with(density) { 62.dp.toPx() }

    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Settled,
            anchors = DraggableAnchors {
                DragValue.Settled at 0f
                DragValue.Revealed at -revealWidth
            },
        )
    }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = anchoredDraggableState,
        positionalThreshold = { distance: Float -> distance * 0.5f },
        animationSpec = spring(),
    )

    // 외부 상태(삭제버튼이 표시된 아이템의 Id) 변화에 따른 반응
    LaunchedEffect(isRevealed) {
        if (!isRevealed) {
            anchoredDraggableState.animateTo(DragValue.Settled)
        }
    }

    // 내부 상태(스와이프 상태) 변화에 따른 반응
    LaunchedEffect(Unit) {
        snapshotFlow { anchoredDraggableState.currentValue }
            .collect { value ->
                if (value == DragValue.Revealed) {
                    onReveal()
                }
            }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        BackgroundContent(
            onClick = onDelete,
            modifier = Modifier.matchParentSize(),
        )

        ForegroundContent(
            content = notificationUiModel.content,
            icon = notificationUiModel.iconRes,
            categoryString = notificationUiModel.categoryStringRes,
            isNew = notificationUiModel.notification.isNew,
            daysSinceReceived = notificationUiModel.daysSinceReceived,
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    val x =
                        if (anchoredDraggableState.offset.isNaN()) 0f else anchoredDraggableState.offset
                    IntOffset(x.roundToInt(), 0)
                }
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Horizontal,
                    flingBehavior = flingBehavior
                ),
        )
    }
}

@Composable
private fun ForegroundContent(
    content: String,
    @DrawableRes icon: Int,
    @StringRes categoryString: Int,
    isNew: Boolean,
    daysSinceReceived: DaysSinceReceived?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
) {
    val backgroundColor =
        if (isNew) KuringTheme.colors.mainPrimarySelected
        else KuringTheme.colors.background
    val interaction = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(
                interactionSource = interaction,
                indication = ripple(),
                onClick = onClick,
            )
            .padding(contentPadding),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    tint = KuringTheme.colors.textCaption1,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(categoryString),
                    style = KuringTheme.typography.tag2.ensureLineHeight(),
                    color = KuringTheme.colors.textCaption1,
                )
            }
            Text(
                text = content,
                style = KuringTheme.typography.body1.ensureLineHeight(),
                color = KuringTheme.colors.textBody,
                maxLines = 1,
            )
        }

        daysSinceReceived?.let {
            Text(
                // "오늘", "어제" 문자열 리소스에는 포맷터가 존재하지 않음
                // `it.days`가 무시됨
                text = stringResource(it.stringRes, it.days),
                style = KuringTheme.typography.caption1.ensureLineHeight(),
                color = KuringTheme.colors.textCaption1,
            )
        }
    }
}

@Composable
private fun BackgroundContent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(KuringTheme.colors.red)
            .clickable(onClick = onClick)
            .padding(end = 18.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = stringResource(notification_swipe_to_delete_box_delete),
            style = KuringTheme.typography.caption1,
            color = KuringTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeToRevealDeletePreview(
    @PreviewParameter(NotificationPreviewParameterProvider::class) notification: Notification
) {
    SwipeToRevealDeleteBox(
        notificationUiModel = NotificationUiModel(notification),
        onReveal = {},
        onDelete = {},
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(5f)
    )
}
