package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.data.model.SavedNotice

fun List<PushEntity>.toPushList(): List<Push> {
    return map {
        val subjectAndTag = splitSubjectAndTag(it.subject.trim())
        Push(
            articleId = it.articleId,
            category = it.category,
            postedDate = it.postedDate,
            subject = subjectAndTag.first,
            baseUrl = it.baseUrl,
            isNew = it.isNew,
            receivedDate = it.receivedDate,
            tag = subjectAndTag.second
        )
    }
}

fun List<NoticeEntity>.toNoticeList() = map { it.toNotice() }

fun NoticeEntity.toNotice(): Notice {
    val (subject, tag) = splitSubjectAndTag(subject)
    return Notice(
        postedDate = postedDate,
        subject = subject,
        category = category,
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
