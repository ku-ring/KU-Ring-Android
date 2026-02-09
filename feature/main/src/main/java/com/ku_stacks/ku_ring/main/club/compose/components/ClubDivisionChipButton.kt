package com.ku_stacks.ku_ring.main.club.compose.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.ensureLineHeight
import com.ku_stacks.ku_ring.domain.ClubDivision

/**
 * 동아리 소속을 나타내고, 선택할 수 있는 버튼이다.
 *
 * @param item 동아리 소속
 * @param isSelected 선택 여부
 * @param modifier 동아리 소속 버튼에 적용될 [Modifier]
 * @param onClick 동아리 소속 버튼을 클릭했을 때 실행할 콜백
 */
@Composable
internal fun ClubDivisionChipButton(
    item: ClubDivision,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (ClubDivision) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    val transition = updateTransition(targetState = isSelected, label = "ChipSelectionTransition")
    val containerColor by transition.animateColor(label = "containerColor") { selected ->
        if (selected) KuringTheme.colors.mainPrimarySelected else KuringTheme.colors.background
    }
    val contentColor by transition.animateColor(label = "contentColor") { selected ->
        if (selected) KuringTheme.colors.mainPrimary else KuringTheme.colors.textCaption1
    }
    val borderColor by transition.animateColor(label = "borderColor") { selected ->
        if (selected) KuringTheme.colors.mainPrimary else KuringTheme.colors.gray200
    }
    
    TextButton(
        onClick = { onClick(item) },
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 8.dp,
        ),
        interactionSource = interactionSource,
        modifier = modifier,
    ) {
        Text(
            text = item.koreanName,
            style = KuringTheme.typography.caption1_1.ensureLineHeight(),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubDivisionChipButtonPreview() {
    KuringTheme {
        var isSelected by remember { mutableStateOf(false) }
        
        ClubDivisionChipButton(
            item = ClubDivision.CENTRAL,
            isSelected = isSelected,
            onClick = { isSelected = !isSelected },
        )
    }
}
