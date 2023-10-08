package com.ku_stacks.ku_ring.notice.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.util.splitSubjectAndTag
import timber.log.Timber

fun List<NoticeEntity>.toNoticeList() = map { it.toNotice() }

fun NoticeEntity.toNotice(): Notice {
    if (this.subject.isEmpty()) {
        Timber.e("Notice.subject is empty: $this")
    }

    val (subject, tag) = splitSubjectAndTag(subject)
    return Notice(
        postedDate = postedDate,
        subject = subject,
        category = category,
        department = department,
        url = url,
        articleId = articleId,
        isNew = isNew,
        isRead = isRead,
        isSubscribing = false,
        isSaved = isSaved,
        isReadOnStorage = isReadOnStorage,
        tag = tag
    )
}