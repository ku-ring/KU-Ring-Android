package com.ku_stacks.ku_ring.notice_storage.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.NoticeItem
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.ui_util.preview_data.previewNotices

@Composable
internal fun StoredNotice(
    notice: Notice,
    isSelectModeEnabled: Boolean,
    toggleNoticeSelection: (Notice) -> Unit,
    onNoticeClick: (Notice) -> Unit,
    selectedNoticeIds: Set<String>,
    modifier: Modifier = Modifier,
) {
    NoticeItem(
        notice = notice,
        onClick = {
            if (isSelectModeEnabled) {
                toggleNoticeSelection(notice)
            } else {
                onNoticeClick(notice)
            }
        },
        modifier = modifier,
    ) {
        if (isSelectModeEnabled) {
            NoticeSelectionStateImage(
                isSelected = notice.articleId in selectedNoticeIds,
                modifier = Modifier
                    .padding(start = 8.dp),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun StoredNoticePreview() {
    val notice = previewNotices.first()
    KuringTheme {
        StoredNotice(
            notice = notice,
            isSelectModeEnabled = true,
            toggleNoticeSelection = { },
            onNoticeClick = { },
            selectedNoticeIds = setOf(notice.articleId),
        )
    }
}