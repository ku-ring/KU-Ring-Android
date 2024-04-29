package com.ku_stacks.ku_ring.main.archive.compose.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices

@Composable
internal fun ArchivedNotices(
    notices: List<Notice>,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    isSelectModeEnabled: Boolean,
    toggleNoticeSelection: (Notice) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = notices,
            key = { it.articleId },
        ) { notice ->
            StoredNotice(
                notice = notice,
                isSelectModeEnabled = isSelectModeEnabled,
                toggleNoticeSelection = toggleNoticeSelection,
                onNoticeClick = onNoticeClick,
                selectedNoticeIds = selectedNoticeIds,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun StoredNoticesPreview() {
    KuringTheme {
        ArchivedNotices(
            notices = previewNotices,
            onNoticeClick = {},
            selectedNoticeIds = emptySet(),
            isSelectModeEnabled = false,
            toggleNoticeSelection = {},
        )
    }
}