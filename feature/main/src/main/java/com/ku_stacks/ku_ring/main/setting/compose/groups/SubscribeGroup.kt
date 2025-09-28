package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingGroup
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingSwitch

@Composable
internal fun SubscribeGroup(
    onNavigateToEditSubscription: () -> Unit,
    isExtNotificationEnabled: Boolean,
    onExtNotificationEnabledToggle: (Boolean) -> Unit,
    isAcademicNotificationEnabled: Boolean,
    onAcademicNotificationEnabledToggle: (Boolean) -> Unit,
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

            SettingSwitch(
                iconId = R.drawable.ic_bell_v2,
                titleId = R.string.setting_subscribe_notifications,
                checked = isExtNotificationEnabled,
                onCheckedChange = onExtNotificationEnabledToggle,
                descriptionId = R.string.setting_subscribe_others_caption,
            )
            SettingSwitch(
                iconId = R.drawable.ic_bell_v2,
                titleId = R.string.setting_subscribe_academic_events,
                checked = isAcademicNotificationEnabled,
                onCheckedChange = onAcademicNotificationEnabledToggle,
                descriptionId = R.string.setting_subscribe_academic_events_caption,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun SubscribeGroupPreview() {
    var isExtNotificationEnabled by remember { mutableStateOf(false) }
    var isAcademicNotificationEnabled by remember { mutableStateOf(false) }
    KuringTheme {
        SubscribeGroup(
            onNavigateToEditSubscription = { },
            isExtNotificationEnabled = isExtNotificationEnabled,
            onExtNotificationEnabledToggle = {
                isExtNotificationEnabled = !isExtNotificationEnabled
            },
            isAcademicNotificationEnabled = isAcademicNotificationEnabled,
            onAcademicNotificationEnabledToggle = {
                isAcademicNotificationEnabled = !isAcademicNotificationEnabled
            },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}