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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
    val contentHeight = 70.dp
    val containerColor = KuringTheme.colors.background

    val isResetButtonVisible = selectedDivisions.isNotEmpty()
    val paddingValues = if (isResetButtonVisible) PaddingValues(start = 20.dp)
    else PaddingValues()

    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .padding(end = 11.dp),
    ) {
        LazyRow(
            modifier = Modifier
                .padding(paddingValues)
                .height(contentHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 60.dp, top = 16.dp, bottom = 16.dp),
        ) {
            stickyHeader {
                val resetButtonShape = RoundedCornerShape(24.dp)
                AnimatedVisibility(
                    visible = isResetButtonVisible,
                    enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
                    exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
                    modifier = Modifier
                        // 버튼의 좌측 영역을 배경색으로 채워서 아이템들이 보이지 않도록 합니다.
                        .background(
                            color = containerColor,
                            shape = resetButtonShape.copy(
                                topStart = CornerSize(0.dp),
                                bottomStart = CornerSize(0.dp),
                            ),
                        )
                ) {
                    ResetButton(
                        onClick = onResetClick,
                        shape = resetButtonShape,
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

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .background(color = containerColor)
                .overlayingGradient(
                    start = 30.dp,
                    colors = listOf(
                        containerColor.copy(alpha = 0f),
                        containerColor.copy(alpha = 0.2f),
                        containerColor.copy(alpha = 0.9f),
                        containerColor.copy(alpha = 1f),
                    ),
                ),
        ) {
            ExpandButton(
                onClick = onExpandClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ResetButton(
    shape: Shape,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 6.5.dp),
    onClick: () -> Unit,
) {
    Surface(
        color = KuringTheme.colors.background,
        border = BorderStroke(width = 1.dp, color = KuringTheme.colors.gray200),
        shape = shape,
        modifier = modifier
            .fillMaxHeight()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
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
    Surface(
        color = KuringTheme.colors.background,
        modifier = modifier
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

        Box(
            Modifier
                .background(KuringTheme.colors.background)
                .padding(10.dp)
        ) {
            ClubDivisionChipButtonGroup(
                selectedDivisions = selectedDivisions,
                onChipClick = { item ->
                    if (selectedDivisions.contains(item)) {
                        selectedDivisions.remove(item)
                    } else {
                        selectedDivisions.add(item)
                    }
                },
                onResetClick = {},
                onExpandClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KuringTheme.colors.background),
            )
        }
    }
}
