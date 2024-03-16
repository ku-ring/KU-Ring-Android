package com.ku_stacks.ku_ring.notice_storage.compose.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices

@Composable
internal fun StoredNotices(
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
        StoredNotices(
            notices = previewNotices,
            onNoticeClick = {},
            selectedNoticeIds = emptySet(),
            isSelectModeEnabled = false,
            toggleNoticeSelection = {},
        )
    }
}