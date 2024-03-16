package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.util.concatSubjectAndTag

fun Notice.toEntity(): NoticeEntity = NoticeEntity(
    articleId = articleId,
    category = category,
    department = department,
    subject = concatSubjectAndTag(subject, tag),
    postedDate = postedDate,
    url = url,
    isNew = isNew,
    isRead = isRead,
    isSaved = isSaved,
    isReadOnStorage = isReadOnStorage,
    isImportant = isImportant,
)