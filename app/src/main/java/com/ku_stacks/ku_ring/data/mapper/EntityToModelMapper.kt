package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.DepartmentEntity
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Push
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

fun List<DepartmentEntity>.toDepartmentList() = map { it.toDepartment() }

fun DepartmentEntity.toDepartment() = Department(
    name = name,
    shortName = shortName,
    koreanName = koreanName,
    isSubscribed = isSubscribed,
    isSelected = false,
    isNotificationEnabled = false,
)