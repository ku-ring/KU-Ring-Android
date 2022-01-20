package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.model.Push

/** 이전 Item과 날짜를 비교해서 날짜 표시 여부 계산 */

fun List<PushEntity>.toPushList(): List<Push> {
    return mapIndexed { idx, it ->
        val isNewDay = if (idx == 0) {
            true
        } else {
            val prevItem = this[idx - 1]
            prevItem.postedDate != it.postedDate
        }

        val subjectAndTag = splitSubjectAndTag(it.subject.trim())
        Push(
            articleId = it.articleId,
            category = it.category,
            postedDate = it.postedDate,
            subject = subjectAndTag.first,
            baseUrl = it.baseUrl,
            isNew = it.isNew,
            receivedDate = it.receivedDate,
            isNewDay = isNewDay,
            tag = subjectAndTag.second
        )
    }
}