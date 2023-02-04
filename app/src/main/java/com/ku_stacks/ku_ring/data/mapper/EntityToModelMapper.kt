package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.db.SavedNoticeEntity
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

fun List<SavedNoticeEntity>.toSavedNoticeList(): List<SavedNotice> = map { it.toSavedNotice() }

fun SavedNoticeEntity.toSavedNotice(): SavedNotice =
    SavedNotice(articleId, category, baseUrl, postedDate, subject)