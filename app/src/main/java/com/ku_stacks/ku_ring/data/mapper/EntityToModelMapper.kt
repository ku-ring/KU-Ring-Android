package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.model.Push

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