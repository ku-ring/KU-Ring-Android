package com.ku_stacks.ku_ring.domain

data class User(
    val email: String,
    val nickName: String,
) {
    companion object {
        val EMPTY = User(
            email = "",
            nickName = "",
        )
    }
}
