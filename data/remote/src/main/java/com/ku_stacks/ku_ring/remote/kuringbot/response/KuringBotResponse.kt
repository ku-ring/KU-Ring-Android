package com.ku_stacks.ku_ring.remote.kuringbot.response

import kotlinx.serialization.Serializable

@Serializable
data class KuringBotResponse(
    val resultMsg: String,
    val resultCode: Int,
    val isSuccess: Boolean,
)
