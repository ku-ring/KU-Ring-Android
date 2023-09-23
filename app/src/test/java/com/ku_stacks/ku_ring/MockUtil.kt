package com.ku_stacks.ku_ring

import com.ku_stacks.ku_ring.data.api.response.UserListResponse
import com.ku_stacks.ku_ring.data.api.response.UserResponse
import com.ku_stacks.ku_ring.data.db.DepartmentEntity
import com.ku_stacks.ku_ring.data.db.PushEntity
import org.mockito.Mockito

object MockUtil {

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    fun mockDepartmentEntity() = DepartmentEntity(
        name = "smart_ict_convergence",
        shortName = "sicte",
        koreanName = "스마트ICT융합공학과",
        isSubscribed = false,
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

    fun mockUserResponse() = UserResponse(nickname = "kuring", userId = "kuring")

    fun mockUserListResponse(): UserListResponse = UserListResponse(
        "abcde"
            .map { it.toString() }
            .map { s -> UserResponse(nickname = s, userId = s) }
    )
}