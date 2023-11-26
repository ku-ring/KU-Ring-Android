package com.ku_stacks.ku_ring.push.mapper

import com.ku_stacks.ku_ring.domain.Push
import com.ku_stacks.ku_ring.local.entity.PushEntity
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.util.isOnlyAlphabets
import com.ku_stacks.ku_ring.util.splitSubjectAndTag

fun List<PushEntity>.toPushList(): List<Push> = map { it.toPush() }

fun PushEntity.toPush(): Push {
    val subjectAndTag = splitSubjectAndTag(subject.trim())
    // DO NOT ERASE: legacy version stores category to korean.
    val categoryEng = if (category.isOnlyAlphabets()) {
        category
    } else {
        WordConverter.convertKoreanToEnglish(category)
    }
    return Push(
        articleId = articleId,
        category = categoryEng,
        postedDate = postedDate,
        subject = subjectAndTag.first,
        fullUrl = fullUrl,
        isNew = isNew,
        receivedDate = receivedDate,
        tag = subjectAndTag.second
    )
}