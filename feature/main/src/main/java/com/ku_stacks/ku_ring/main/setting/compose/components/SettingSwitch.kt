package com.ku_stacks.ku_ring.main.setting.compose.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.KuringSwitch
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun SettingSwitch(
    @DrawableRes iconId: Int,
    @StringRes titleId: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes descriptionId: Int,
) {
    SettingItem(
        iconId = iconId,
        title = stringResource(titleId),
        onClick = null,
        content = {
            KuringSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    )
    Text(
        text = stringResource(descriptionId),
        style = KuringTheme.typography.tag.copy(
            color = KuringTheme.colors.textCaption1,
            letterSpacing = 0.15.sp,
            fontWeight = FontWeight.Normal,
        ),
        modifier = Modifier
            .padding(start = 56.dp)
            .offset { IntOffset(0, -(10.dp.roundToPx())) },
    )
}