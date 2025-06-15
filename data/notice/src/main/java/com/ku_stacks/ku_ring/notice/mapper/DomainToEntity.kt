package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.entity.NoticeEntity

fun Notice.toEntity(): NoticeEntity = NoticeEntity(
    articleId = articleId,
    id = id,
    category = category,
    department = department,
    subject = subject,
    postedDate = postedDate,
    url = url,
    isNew = isNew,
    isRead = isRead,
    isSaved = isSaved,
    isReadOnStorage = isReadOnStorage,
    isImportant = isImportant,
    commentCount = commentCount,
)