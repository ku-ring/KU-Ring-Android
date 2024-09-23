package com.ku_stacks.ku_ring.remote.user.response

data class KuringBotQueryCountResponse(
    val code: Int,
    val message: String,
    val data: KuringBotQueryCountData,
)

data class KuringBotQueryCountData(
    val leftAskCount: Int,
    val maxAskCount: Int,
)
