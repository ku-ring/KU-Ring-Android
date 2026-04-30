package com.ku_stacks.ku_ring.remote.notice.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("name") val name: String,
    @SerialName("hostPrefix") val shortName: String,
    @SerialName("korName") val koreanName: String,
)
