package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.KuringSwitch
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.Gray300
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingGroup
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun SubscribeGroup(
    onNavigateToEditSubscription: () -> Unit,
    isExtNotificationEnabled: Boolean,
    onExtNotificationEnabledToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SettingGroup(groupTitle = stringResource(id = R.string.setting_subscribe_title)) {
            SettingItem(
                iconId = R.drawable.ic_bell_v2,
                title = stringResource(id = R.string.setting_subscribe_notifications),
                onClick = onNavigateToEditSubscription,
                content = { ChevronIcon() },
            )
            SettingItem(
                iconId = R.drawable.ic_bell_v2,
                title = stringResource(id = R.string.setting_subscribe_others),
                onClick = null,
                content = {
                    KuringSwitch(
                        checked = isExtNotificationEnabled,
                        onCheckedChange = {
                            onExtNotificationEnabledToggle(!isExtNotificationEnabled)
                        },
                    )
                },
            )
        }

        Text(
            text = stringResource(id = R.string.setting_subscribe_others_caption),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(400),
                color = Gray300,
                letterSpacing = 0.15.sp,
            ),
            modifier = Modifier
                .padding(start = 56.dp)
                .offset { IntOffset(0, -(10.dp.roundToPx())) },
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SubscribeGroupPreview() {
    var enabled by remember { mutableStateOf(false) }
    KuringTheme {
        SubscribeGroup(
            onNavigateToEditSubscription = { },
            isExtNotificationEnabled = enabled,
            onExtNotificationEnabledToggle = { enabled = !enabled },
        )
    }
}