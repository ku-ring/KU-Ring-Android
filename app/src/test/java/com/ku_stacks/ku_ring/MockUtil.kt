package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.response.UserListResponse
import com.ku_stacks.ku_ring.data.api.response.UserResponse
import com.ku_stacks.ku_ring.data.db.DepartmentEntity
import org.mockito.Mockito

object MockUtil {

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    fun mockDepartmentEntity() = DepartmentEntity(
        name = "smart_ict_convergence",
        shortName = "sicte",
        koreanName = "스마트ICT융합공학과",
        isSubscribed = false,
    )

    fun mockUserResponse() = UserResponse(nickname = "kuring", userId = "kuring")

    fun mockUserListResponse(): UserListResponse = UserListResponse(
        "abcde"
            .map { it.toString() }
            .map { s -> UserResponse(nickname = s, userId = s) }
    )
}