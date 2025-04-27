package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.entity.NoticeEntity

fun List<NoticeEntity>.toNoticeList() = map { it.toNotice() }

fun NoticeEntity.toNotice(): Notice {
    return Notice(
        postedDate = postedDate,
        subject = subject,
        category = category,
        department = department,
        url = url,
        articleId = articleId,
        id = id,
        isNew = isNew,
        isRead = isRead,
        isSubscribing = false,
        isSaved = isSaved,
        isReadOnStorage = isReadOnStorage,
        isImportant = isImportant,
        tag = emptyList(),
    )
}