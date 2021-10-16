package com.ku_stacks.ku_ring.notice_usecase

import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.di.NetworkModule.provideNoticeClient
import com.ku_stacks.ku_ring.di.NetworkModule.provideNoticeService
import com.ku_stacks.ku_ring.di.NetworkModule.provideOkHttpClient
import com.ku_stacks.ku_ring.di.NetworkModule.provideRetrofit
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoticeNetworkTest {

    lateinit var noticeClient: NoticeClient

    @Before
    fun setUp() {
        noticeClient = provideNoticeClient(provideNoticeService(provideRetrofit(provideOkHttpClient())))
    }

    @Test
    fun `공지사항 fetch Test`() {
        val result = noticeClient.fetchNotice(type = "bch",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }
}