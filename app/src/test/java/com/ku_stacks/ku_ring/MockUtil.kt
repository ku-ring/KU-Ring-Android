package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.api.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.DepartmentNoticeResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeResponse
import com.ku_stacks.ku_ring.data.api.response.SubscribeListResponse
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.data.model.Notice
import org.mockito.Mockito

object MockUtil {

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    fun mockNoticeResponseList() = NoticeListResponse(
        resultMsg = "성공",
        resultCode = 200,
        noticeResponse = listOf(mockNoticeResponse()),
    )

    private fun mockNoticeResponse() = NoticeResponse(
        articleId = "5b4a11b",
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        category = "bachelor",
        url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isImportant = false,
    )

    fun mockNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        subject = "2023학년도 전과 선발자 안내",
        postedDate = "20230208",
        url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isNew = false,
        isRead = false,
        isSaved = false,
        isReadOnStorage = false,
    )

    fun mockReadNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        subject = "2023학년도 전과 선발자 안내",
        postedDate = "20230208",
        url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isNew = false,
        isRead = true,
        isSaved = false,
        isReadOnStorage = false,
    )

    fun mockNotice() = Notice(
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        category = "bachelor",
        url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5b4a11b",
        articleId = "5b4a11b",
        isNew = true,
        isRead = false,
        isSubscribing = false,
        isSaved = false,
        isReadOnStorage = false,
        tag = emptyList()
    )

    fun mockPushEntity() = PushEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        fullUrl = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do",
        isNew = false,
        receivedDate = "20220207-132051"
    )

    fun mockSubscribeListResponse() = SubscribeListResponse(
        isSuccess = true,
        resultMsg = "성공",
        resultCode = 200,
        categoryList = listOf("bachelor", "employment")
    )

    fun mockSubscribeRequest() = SubscribeRequest(
        token = "AAAAn6eQM_Y:APA91bES4rjrFwPY5i_Hz-kT0u32SzIUxreYm9qaQHZeYKGGV_BmHZNJhHvlDjyQA6LveNdxCVrwzsq78jgsnCw8OumbtM5L3cc17XgdqZ_dlpsPzR7TlJwBFTXRFLPst663IeX27sb0",
        categories = listOf("bachelor", "scholarship")
    )

    fun mockDefaultResponse() = DefaultResponse(
        isSuccess = true,
        resultMsg = "성공",
        resultCode = 200
    )

    fun mockSucceededDepartmentNoticeListResponse(dataSize: Int) = DepartmentNoticeListResponse(
        code = 200,
        message = "공지 조회에 성공하였습니다",
        data = (1..dataSize).map {
            DepartmentNoticeResponse(
                articleId = it.toString(),
                postedDate = "2023-05-02",
                url = "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&seq=182677",
                subject = "2023학년도 진로총조사 설문 요청",
                category = "department",
                important = false
            )
        }
    )

    fun mockEmptyDepartmentNoticeListResponse() = DepartmentNoticeListResponse(
        code = 200,
        message = "공지 조회에 성공하였습니다",
        data = emptyList()
    )
}