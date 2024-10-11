package com.ku_stacks.ku_ring.notice.test

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.*
import com.ku_stacks.ku_ring.remote.util.DefaultResponse

object NoticeTestUtil {
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

    fun fakeNotice() = Notice(
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
        isSubscribing = false,
        tag = emptyList(),
    )

    fun fakeSubscribeListResponse() = SubscribeListResponse(
        resultMsg = "성공",
        resultCode = 200,
        categoryList = listOf(
            CategoryResponse("student", "stu", "학생"),
            CategoryResponse("employment", "emp", "취창업"),
        ),
    )

    fun fakeSubscribeRequest() = SubscribeRequest(
        categories = mutableListOf("bachelor", "scholarship"),
    )

    fun fakeDefaultResponse() = DefaultResponse(
        resultCode = 200,
        resultMsg = "성공",
        data = null,
    )

    fun fakeNoticeListResponse() = NoticeListResponse(
        resultMsg = "성공",
        resultCode = 200,
        noticeResponse = listOf(fakeNoticeResponse()),
    )

    fun fakeNoticeResponse() = NoticeResponse(
        articleId = "5b4a11b",
        category = "bachelor",
        isImportant = false,
        subject = "2023학년도 전과 선발자 안내",
        postedDate = "20230208",
        url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
    )

    fun fakeEmptyNoticeListResponse() = NoticeListResponse(
        resultMsg = "성공",
        resultCode = 200,
        noticeResponse = emptyList(),
    )

    fun fakeDepartmentNoticeListResponse(dataSize: Int) = DepartmentNoticeListResponse(
        code = 200,
        message = "공지 조회에 성공하였습니다",
        data = (1..dataSize).map {
            DepartmentNoticeResponse(
                articleId = it.toString(),
                category = "bachelor",
                subject = "2023학년도 전과 선발자 안내",
                postedDate = "20230208",
                url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
                important = false,
            )
        }
    )

    fun fakeEmptyDepartmentNoticeListResponse() = DepartmentNoticeListResponse(
        code = 200,
        message = "공지 조회에 성공하였습니다",
        data = emptyList()
    )
}