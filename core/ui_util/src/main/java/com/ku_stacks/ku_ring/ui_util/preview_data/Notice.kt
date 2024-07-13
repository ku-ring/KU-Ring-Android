package com.ku_stacks.ku_ring.ui_util.preview_data

import com.ku_stacks.ku_ring.domain.Notice

val previewNotices = (1..10).map {
    Notice(
        articleId = "5b4a11b$it",
        category = "bachelor",
        subject = "2023학년도 전과 선발자 안내",
        postedDate = "2023.02.08",
        url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isNew = false,
        isRead = false,
        isSubscribing = false,
        isSaved = false,
        isReadOnStorage = false,
        isImportant = it < 3,
        tag = listOf("장학", "취창업"),
    )
}