package com.ku_stacks.ku_ring.main.club.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.ClubDivision
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Composable
fun ClubDivisionChipButtonGroup(
    isSelectedMap: ImmutableMap<ClubDivision, Boolean>,
    onChipClick: (ClubDivision) -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isResetButtonVisible by remember(isSelectedMap) {
        derivedStateOf { isSelectedMap.containsValue(true) }
    }

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        stickyHeader {
            AnimatedVisibility(
                visible = isResetButtonVisible,
                enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
            ) {
                ResetButton(
                    onClick = onResetClick,
                    modifier = Modifier,
                )
            }
        }

        items(
            items = ClubDivision.entries,
            key = { item -> item.name },
        ) { item ->
            ClubDivisionChipButton(
                item = item,
                isSelected = isSelectedMap[item] ?: false,
                onClick = onChipClick,
            )
        }
    }
}

@Composable
private fun ResetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(24.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        color = KuringTheme.colors.background,
        border = BorderStroke(width = 1.dp, color = KuringTheme.colors.gray200),
        shape = shape,
        modifier = modifier
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
                .padding(
                    horizontal = 14.dp,
                    vertical = 6.5.dp,
                )
                .size(24.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubDivisionChipButtonGroupPreview() {
    KuringTheme {
        val isSelectedMap = remember {
            mutableStateMapOf<ClubDivision, Boolean>().apply {
                putAll(ClubDivision.entries.associateWith { false })
            }
        }

        ClubDivisionChipButtonGroup(
            isSelectedMap = isSelectedMap.toImmutableMap(),
            onChipClick = { item ->
                isSelectedMap[item] = !isSelectedMap[item]!!
            },
            onResetClick = {
                ClubDivision.entries.forEach { item ->
                    isSelectedMap[item] = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
