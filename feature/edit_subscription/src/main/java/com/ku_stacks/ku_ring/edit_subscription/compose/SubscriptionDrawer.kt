package com.ku_stacks.ku_ring.edit_subscription.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ku_stacks.ku_ring.edit_subscription.R
import com.ku_stacks.ku_ring.edit_subscription.SubscriptionUiModel
import com.ku_stacks.ku_ring.ui_util.compose.NonLazyGrid
import com.ku_stacks.ku_ring.ui_util.compose.theme.KuringTheme
import com.ku_stacks.ku_ring.ui_util.compose.theme.SfProDisplay

@Composable
fun SubscriptionDrawer(
    title: String,
    subscriptionItems: List<SubscriptionUiModel>,
    onHeaderClick: () -> Unit,
    onItemClick: (SubscriptionUiModel) -> Unit,
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    columns: Int = 1,
) {
    val horizontalMargin = 16.dp
    val roundedCornerShape = RoundedCornerShape(16.dp)

    ConstraintLayout(
        modifier = modifier
            .clip(roundedCornerShape)
            .background(Color.White.copy(alpha = .2f))
            .animateContentSize()
    ) {
        val (header, grid, emptyText) = createRefs()
        SubscriptionHeader(
            title = title,
            isOpen = isOpen,
            horizontalMargin = horizontalMargin,
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                    if (!isOpen) {
                        bottom.linkTo(parent.bottom)
                    }
                }
                .clip(roundedCornerShape)
                .clickable(onClick = onHeaderClick),
        )
        if (isOpen) {
            if (subscriptionItems.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.subscribe_items_empty),
                    fontFamily = SfProDisplay,
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.constrainAs(emptyText) {
                        top.linkTo(header.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 34.dp)
                    },
                )
            } else {
                SubscriptionGrid(
                    columns = columns,
                    subscriptionItems = subscriptionItems,
                    onClick = onItemClick,
                    modifier = Modifier.constrainAs(grid) {
                        top.linkTo(header.bottom, margin = 3.dp)
                        start.linkTo(parent.start, margin = horizontalMargin)
                        end.linkTo(parent.end, margin = horizontalMargin)
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                        width = Dimension.matchParent
                    }
                )
            }
        }
    }
}

@Composable
private fun SubscriptionHeader(
    title: String,
    isOpen: Boolean,
    horizontalMargin: Dp,
    modifier: Modifier = Modifier,
) {
    val arrowAngle by animateFloatAsState(targetValue = if (isOpen) -90f else 90f)

    ConstraintLayout(modifier = modifier) {
        val (titleText, arrow) = createRefs()
        SubscriptionTitle(
            title = title,
            modifier = Modifier.constrainAs(titleText) {
                top.linkTo(parent.top, margin = 11.dp)
                bottom.linkTo(parent.bottom, margin = 11.dp)
                start.linkTo(parent.start, margin = horizontalMargin)
            }
        )
        Image(
            painter = painterResource(R.drawable.ic_arrow),
            contentDescription = stringResource(R.string.subscribe_header_description),
            modifier = Modifier
                .rotate(arrowAngle)
                .constrainAs(arrow) {
                    top.linkTo(titleText.top)
                    bottom.linkTo(titleText.bottom)
                    end.linkTo(parent.end, margin = horizontalMargin)
                }
                .scale(0.7f),
            colorFilter = ColorFilter.tint(Color.White),
        )
    }
}

@Composable
private fun SubscriptionGrid(
    columns: Int,
    subscriptionItems: List<SubscriptionUiModel>,
    onClick: (SubscriptionUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    NonLazyGrid(
        columns = columns,
        itemCount = subscriptionItems.size,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        SubscriptionItem(
            subscriptionUiModel = subscriptionItems[it],
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun SubscriptionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        fontFamily = SfProDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = Color.White,
    )
}

@Composable
fun SubscriptionItem(
    subscriptionUiModel: SubscriptionUiModel,
    onClick: (SubscriptionUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = subscriptionUiModel.isNotificationEnabled
    val backgroundColor by animateColorAsState(if (isEnabled) Color.White else Color.Transparent)
    val textColor by animateColorAsState(if (isEnabled) MaterialTheme.colors.primary else Color.White)

    val shape = RoundedCornerShape(8.dp)
    TextButton(
        onClick = { onClick(subscriptionUiModel) },
        modifier = modifier
            .border(1.dp, Color.White, shape)
            .clip(shape)
            .background(backgroundColor)
            .height(44.dp)
            .defaultMinSize(minWidth = 88.dp),
    ) {
        Text(
            text = subscriptionUiModel.content,
            fontFamily = SfProDisplay,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = textColor,
        )
    }
}

@Preview()
@Composable
fun SubscriptionTitlePreview() {
    KuringTheme {
        SubscriptionTitle(
            title = "대학 공지 카테고리",
            modifier = Modifier
                .background(Color.Black)
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
private fun SubscriptionItemPreview() {
    KuringTheme {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colors.primary)
                .background(Color.White.copy(alpha = 0.2f))
        ) {
            SubscriptionItem(
                modifier = Modifier.align(Alignment.Center),
                subscriptionUiModel = SubscriptionUiModel(
                    content = "학사",
                    isNotificationEnabled = true,
                ),
                onClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun SubscriptionDrawerPreview() {
    val items = listOf("학사", "장학", "취창업", "국제", "학생", "산학", "일반", "도서관").mapIndexed { index, s ->
        SubscriptionUiModel(s, isNotificationEnabled = (index % 2 == 0))
    }
    var isOpen by remember { mutableStateOf(true) }
    KuringTheme {
        SubscriptionDrawer(
            title = "대학 공지 카테고리",
            isOpen = isOpen,
            subscriptionItems = items,
            onHeaderClick = { isOpen = !isOpen },
            onItemClick = {},
            columns = 3,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .background(Color.White.copy(alpha = 0.2f))
                .padding(10.dp),
        )
    }
}

@Preview
@Composable
private fun SubscriptionDrawerPreview_Empty() {
    var isOpen by remember { mutableStateOf(true) }
    KuringTheme {
        SubscriptionDrawer(
            title = "대학 공지 카테고리",
            isOpen = isOpen,
            subscriptionItems = emptyList(),
            onHeaderClick = { isOpen = !isOpen },
            onItemClick = {},
            columns = 3,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .background(Color.White.copy(alpha = 0.2f))
                .padding(10.dp),
        )
    }
}