package com.ku_stacks.ku_ring.remote.club.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubUnsubscribeResponse(
    val bookmarkCount: Int,
)