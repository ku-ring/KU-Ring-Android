package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeResponse
import com.ku_stacks.ku_ring.data.api.response.SubscribeListResponse
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.db.PushEntity
import org.mockito.Mockito

object MockUtil {

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    fun mockNoticeList() = NoticeListResponse(
        isSuccess = true,
        resultMsg = "성공",
        resultCode = 200,
        baseUrl = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do",
        noticeResponse = listOf(mockNoticeResponse())
    )

    private fun mockNoticeResponse() = NoticeResponse(
        articleId = "5b4a11b",
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        category = "bachelor"
    )

    fun mockNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        isNew = false,
        isRead = false
    )

    fun mockReadNoticeEntity() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        isNew = false,
        isRead = true
    )

    fun mockPushEntity() = PushEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        postedDate = "20220203",
        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
        baseUrl = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do",
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
}