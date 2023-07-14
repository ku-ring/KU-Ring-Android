package com.ku_stacks.ku_ring.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.data.api.SendbirdClient
import com.ku_stacks.ku_ring.data.api.response.UserListResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SendbirdRepositoryTest {

    private val sendbirdClient: SendbirdClient = Mockito.mock(SendbirdClient::class.java)
    private lateinit var sendbirdRepository: SendbirdRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        sendbirdRepository = SendbirdRepositoryImpl(sendbirdClient)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
    }

    @Test
    fun `given no user then false`() {
        val nickname = "kuring"
        val mockResponse = UserListResponse(emptyList())
        Mockito.`when`(sendbirdClient.fetchNicknameList(nickname))
            .thenReturn(Single.just(mockResponse))

        val userId = "someUserId"
        sendbirdRepository.hasDuplicateNickname(nickname, userId)
            .test()
            .assertComplete()
            .assertValue(false)
    }

    @Test
    fun `given no duplicates then false`() {
        val mockResponse = MockUtil.mockUserResponse()
        val (nickname, userId) = mockResponse
        Mockito.`when`(sendbirdClient.fetchNicknameList(nickname))
            .thenReturn(Single.just(UserListResponse(listOf(mockResponse))))

        sendbirdRepository.hasDuplicateNickname(nickname, userId)
            .test()
            .assertComplete()
            .assertValue(false)
    }

    @Test
    fun `given a duplicate then true`() {
        val mockResponse = MockUtil.mockUserListResponse()
        val (nickname, userId) = mockResponse.users[0]
        Mockito.`when`(sendbirdClient.fetchNicknameList(nickname))
            .thenReturn(Single.just(mockResponse))

        sendbirdRepository.hasDuplicateNickname(nickname, userId)
            .test()
            .assertComplete()
            .assertValue(true)
    }

}