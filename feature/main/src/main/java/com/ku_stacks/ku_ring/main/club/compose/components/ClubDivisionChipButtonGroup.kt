package com.ku_stacks.ku_ring.main.club.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.overlayingGradient
import com.ku_stacks.ku_ring.domain.ClubDivision

@Composable
fun ClubDivisionChipButtonGroup(
    selectedDivisions: Set<ClubDivision>,
    onChipClick: (ClubDivision) -> Unit,
    onResetClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentHeight = 37.dp
    val isResetButtonVisible = selectedDivisions.isNotEmpty()
    val paddingValues =
        if (isResetButtonVisible) PaddingValues(start = 20.dp)
        else PaddingValues()

    Box(
        modifier = modifier
            .height(contentHeight)
            .wrapContentHeight()
            .padding(end = 11.dp),
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 60.dp),
        ) {
            stickyHeader {
                AnimatedVisibility(
                    visible = isResetButtonVisible,
                    enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
                    exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
                ) {
                    ResetButton(
                        size = contentHeight,
                        onClick = onResetClick,
                    )
                }
            }

            items(
                items = ClubDivision.entries,
                key = { item -> item.name },
            ) { item ->
                ClubDivisionChipButton(
                    item = item,
                    isSelected = selectedDivisions.contains(item),
                    onClick = onChipClick,
                )
            }
        }

        ExpandButton(
            onClick = onExpandClick,
            modifier = Modifier
                .align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun ResetButton(
    size: Dp,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 6.5.dp),
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val containerColor = KuringTheme.colors.background

    val roundedCornerDp = 24.dp
    val shape = RoundedCornerShape(roundedCornerDp)
    val backgroundShape = RoundedCornerShape(bottomEnd = roundedCornerDp, topEnd = roundedCornerDp)

    Surface(
        color = containerColor,
        border = BorderStroke(width = 1.dp, color = KuringTheme.colors.gray200),
        shape = shape,
        modifier = modifier
            .height(size)
            .background(color = containerColor, shape = backgroundShape)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick,
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_refresh_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray400,
            modifier = Modifier
                .padding(contentPadding)
                .size(24.dp),
        )
    }
}


@Composable
private fun ExpandButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val backgroundColor = KuringTheme.colors.background

    Surface(
        color = backgroundColor,
        modifier = modifier
            .overlayingGradient(
                colors = listOf(
                    backgroundColor.copy(alpha = 0f),
                    backgroundColor.copy(alpha = 0.2f),
                    backgroundColor.copy(alpha = 0.9f),
                    backgroundColor.copy(alpha = 1f),
                ),
                start = 30.dp
            )
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_down_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
            modifier = Modifier
                .background(backgroundColor)
                .padding(6.dp)
                .size(24.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubDivisionChipButtonGroupPreview() {
    KuringTheme {
        val selectedDivisions = remember {
            mutableStateSetOf(ClubDivision.CENTRAL)
        }

        ClubDivisionChipButtonGroup(
            selectedDivisions = selectedDivisions,
            onChipClick = { item ->
                if (selectedDivisions.contains(item)) {
                    selectedDivisions.remove(item)
                } else {
                    selectedDivisions.add(item)
                }
            },
            onResetClick = {
                selectedDivisions.clear()
            },
            onExpandClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(KuringTheme.colors.background),
        )
    }
}
