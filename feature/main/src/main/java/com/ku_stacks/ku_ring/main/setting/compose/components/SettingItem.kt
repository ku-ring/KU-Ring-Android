package com.ku_stacks.ku_ring.main.setting.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringThemeTest
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun SettingItem(
    @DrawableRes iconId: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    val onClickModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick, role = Role.Button)
    } else {
        modifier
    }

    Row(
        modifier = onClickModifier
            .padding(horizontal = 20.dp)
            .semantics(mergeDescendants = true) { },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
            modifier = Modifier.padding(vertical = 14.dp),
        )
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textTitle,
                letterSpacing = 0.15.sp,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        content()
    }
}

@LightAndDarkPreview
@Composable
private fun SettingItemPreview() {
    var angle by remember { mutableFloatStateOf(0f) }

    KuringThemeTest {
        Column {
            SettingItem(
                iconId = R.drawable.ic_bell,
                title = "공지 구독하기",
                onClick = { angle += 90f },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KuringTheme.colors.background),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron),
                    contentDescription = null,
                    tint = KuringTheme.colors.gray300,
                    modifier = Modifier.rotate(angle)
                )
            }
        }
    }
}