package com.ku_stacks.ku_ring.remote.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KuringBotQueryCountResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: KuringBotQueryCountData,
)

@Serializable
data class KuringBotQueryCountData(
    @SerialName("leftAskCount") val leftAskCount: Int,
    @SerialName("maxAskCount") val maxAskCount: Int,
)
