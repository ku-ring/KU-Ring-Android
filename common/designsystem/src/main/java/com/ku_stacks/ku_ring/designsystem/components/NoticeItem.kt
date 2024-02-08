package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice

@Composable
fun NoticeItem(
    notice: Notice,
    modifier: Modifier = Modifier,
    onClick: (Notice) -> Unit = {},
) {

}

@LightAndDarkPreview
@Composable
private fun NoticeItemPreview() {
    val notice = Notice(
        postedDate = "2023.05.13",
        subject = "2023학년도 1학기 2차 교직 응급처치 및 심폐소생술 실습 안내",
        category = "department",
        department = "cse",
        url = "",
        articleId = "",
        isNew = false,
        isRead = false,
        isSubscribing = false,
        isSaved = false,
        isReadOnStorage = false,
        tag = listOf("지급"),
    )
    KuringTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface),
        ) {
            NoticeItem(notice = notice)
            NoticeItem(notice = notice.copy(isRead = true))
            NoticeItem(notice = notice.copy(isNew = true))
            NoticeItem(notice = notice.copy(isNew = true, isSubscribing = true))
            NoticeItem(notice = notice.copy(isSaved = true))
        }
    }
}