package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.response.UserListResponse
import com.ku_stacks.ku_ring.data.api.response.UserResponse
import org.mockito.Mockito

object MockUtil {

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    fun mockUserResponse() = UserResponse(nickname = "kuring", userId = "kuring")

    fun mockUserListResponse(): UserListResponse = UserListResponse(
        "abcde"
            .map { it.toString() }
            .map { s -> UserResponse(nickname = s, userId = s) }
    )
}