package com.ku_stacks.ku_ring.remote.util

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO by mwy3055: kotlinx serialization으로 통일하기
@Serializable
data class DefaultResponse(
    @SerializedName("code")
    @SerialName("code")
    val resultCode: Int,
    @SerializedName("message")
    @SerialName("message")
    val resultMsg: String,
    @Contextual val data: Any?,
) {
    val isSuccess: Boolean
        get() = (resultCode == 200)
}