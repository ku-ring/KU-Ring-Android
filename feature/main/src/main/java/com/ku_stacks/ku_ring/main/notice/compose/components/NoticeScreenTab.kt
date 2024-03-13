package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.theme.MainPrimary
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.theme.TextCaption1
import com.ku_stacks.ku_ring.main.notice.NoticeScreenTabItem

@Composable
internal fun NoticeTab(
    tab: NoticeScreenTabItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MainPrimary else TextCaption1,
        label = "notice tab text color",
    )
    Text(
        text = tab.koreanName,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(600),
            color = textColor,
        ),
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}