package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.DepartmentEntity
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.model.Department
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.util.isOnlyAlphabets
import timber.log.Timber

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

fun List<DepartmentEntity>.toDepartmentList() = map { it.toDepartment() }

fun DepartmentEntity.toDepartment() = Department(
    name = name,
    shortName = shortName,
    koreanName = koreanName,
    isSubscribed = isSubscribed,
    isSelected = false,
    isNotificationEnabled = false,
)