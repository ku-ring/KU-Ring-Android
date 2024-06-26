package com.ku_stacks.ku_ring.main.setting.compose.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.components.ChevronIcon
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingGroup
import com.ku_stacks.ku_ring.main.setting.compose.components.SettingItem

@Composable
internal fun FeedbackGroup(
    onNavigateToFeedback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingGroup(
        groupTitle = stringResource(id = R.string.setting_feedback_title),
        modifier = modifier,
    ) {
        SettingItem(
            iconId = R.drawable.ic_feedback_v2,
            title = stringResource(id = R.string.setting_feedback_send_feedback),
            onClick = onNavigateToFeedback,
            content = { ChevronIcon() },
        )
    }
}

@LightAndDarkPreview
@Composable
private fun FeedbackGroupPreview() {
    KuringTheme {
        FeedbackGroup(
            onNavigateToFeedback = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}