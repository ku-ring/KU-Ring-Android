package com.ku_stacks.ku_ring.remote.club.request

import kotlinx.serialization.Serializable

@Serializable
data class ClubUnsubscribeRequest(
    val id: Int,
)
