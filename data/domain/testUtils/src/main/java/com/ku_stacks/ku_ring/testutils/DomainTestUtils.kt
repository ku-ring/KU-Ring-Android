package com.ku_stacks.ku_ring.testutils

import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Notice

object DomainTestUtils {
    fun createNotice(
        postedDate: String = "20220203",
        subject: String = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        category: String = "bachelor",
        department: String = "",
        url: String = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5b4a11b",
        articleId: String = "5b4a11b",
        isNew: Boolean = true,
        isRead: Boolean = false,
        isSubscribing: Boolean = false,
        isSaved: Boolean = false,
        isReadOnStorage: Boolean = false,
        isImportant: Boolean = false,
        tag: List<String> = emptyList(),
    ) = Notice(
        postedDate = postedDate,
        subject = subject,
        category = category,
        department = department,
        url = url,
        articleId = articleId,
        isNew = isNew,
        isRead = isRead,
        isSubscribing = isSubscribing,
        isSaved = isSaved,
        isReadOnStorage = isReadOnStorage,
        isImportant = isImportant,
        tag = tag,
    )

    fun createDepartment(
        name: String = "smart_ict_convergence",
        shortName: String = "sicte",
        koreanName: String = "스마트ICT융합공학과",
        isSubscribed: Boolean = false,
        isSelected: Boolean = false,
        isNotificationEnabled: Boolean = false,
    ) = Department(
        name = name,
        shortName = shortName,
        koreanName = koreanName,
        isSubscribed = isSubscribed,
        isSelected = isSelected,
        isNotificationEnabled = isNotificationEnabled,
    )
}