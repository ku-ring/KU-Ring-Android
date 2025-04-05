package com.ku_stacks.ku_ring.user.mapper

import com.ku_stacks.ku_ring.domain.User
import com.ku_stacks.ku_ring.remote.user.response.UserDataResponse

fun UserDataResponse.toDomain() = with(data) {
    User(
        email = email,
        nickName = nickName,
    )
}
