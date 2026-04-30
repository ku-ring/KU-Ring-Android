package com.ku_stacks.ku_ring.remote.user.response

import kotlinx.serialization.Serializable

@Serializable
data class KuringBotQueryCountResponse(
    val code: Int,
    val message: String,
    val data: KuringBotQueryCountData,
)

@Serializable
data class KuringBotQueryCountData(
    val leftAskCount: Int,
    val maxAskCount: Int,
)
