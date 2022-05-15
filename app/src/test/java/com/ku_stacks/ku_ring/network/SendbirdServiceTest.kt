package com.ku_stacks.ku_ring.network

import com.ku_stacks.ku_ring.data.api.SendbirdService
import com.ku_stacks.ku_ring.di.NetworkModule.provideSendbirdOkHttpClient
import com.ku_stacks.ku_ring.di.NetworkModule.provideSendbirdRetrofit
import com.ku_stacks.ku_ring.di.NetworkModule.provideSendbirdService
import org.junit.Before
import org.junit.Test

class SendbirdServiceTest : ApiAbstract<SendbirdService>() {

    private lateinit var service: SendbirdService

    @Before
    fun initService() {
        //service = createService(SendbirdService::class.java)
        service = provideSendbirdService(provideSendbirdRetrofit(provideSendbirdOkHttpClient()))
    }

    @Test
    fun test() {
        val response = service.hasDuplicateNickname("건국오리들입니다").blockingGet()
        print(response.users[0].nickname)
    }
}