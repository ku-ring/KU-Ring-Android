package com.ku_stacks.ku_ring.notice_usecase

import com.ku_stacks.ku_ring.data.api.NoticeService
import com.ku_stacks.ku_ring.di.NetworkModule.provideNoticeService
import com.ku_stacks.ku_ring.di.NetworkModule.provideOkHttpClient
import com.ku_stacks.ku_ring.di.NetworkModule.provideRetrofit
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoticeNetworkTest {

    lateinit var noticeService: NoticeService

    @Before
    fun setUp() {
        noticeService = provideNoticeService(provideRetrofit(provideOkHttpClient()))
    }

    @Test
    fun `bch notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "bch",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `sch notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "sch",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `emp notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "emp",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `nat notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "nat",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `stu notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "stu",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `ind notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "ind",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `nor notice fetch Test`() {
        val result = noticeService.fetchNoticeList(type = "nor",offset = 0, max = 10)
            .blockingGet()

        assertEquals(true, result.isSuccess)
    }

}