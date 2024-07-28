package com.ku_stacks.ku_ring.local.test

import com.ku_stacks.ku_ring.local.entity.DepartmentEntity
import com.ku_stacks.ku_ring.local.entity.KuringBotMessageEntity
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.entity.PushEntity

object LocalTestUtil {
    fun fakeDepartmentEntity() = DepartmentEntity(
        name = "smart_ict_convergence",
        shortName = "sicte",
        koreanName = "스마트ICT융합공학과",
        isSubscribed = false,
        isMainDepartment = false,
    )

    fun fakeNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        department = "",
        subject = "2023학년도 전과 선발자 안내",
        postedDate = "20230208",
        url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isNew = false,
        isRead = false,
        isSaved = false,
        isReadOnStorage = false,
        isImportant = false,
    )

    fun fakeDepartmentNoticeEntity() = NoticeEntity(
        articleId = "182677",
        category = "department",
        department = "cse",
        subject = "2023학년도 진로총조사 설문 요청",
        postedDate = "2023-05-02",
        url = "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&seq=182677",
        isNew = false,
        isRead = false,
        isSaved = false,
        isReadOnStorage = false,
        isImportant = false,
    )

    fun fakePushEntity() = PushEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        postedDate = "2022-01-14 00:50:33",
        subject = "2023학년도 전과 선발자 안내",
        fullUrl = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isNew = true,
        receivedDate = "20220114-005036",
    )

    fun fakeKuringBotMessageEntity(
        message: String = "",
        postedEpochSeconds: Long = 0L,
        isQuery: Boolean = false,
    ) = KuringBotMessageEntity(
        id = 0,
        message = message,
        postedEpochSeconds = postedEpochSeconds,
        isQuery = isQuery,
    )
}