package com.ku_stacks.ku_ring.remote.util

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

// TODO by mwy3055: kotlinx serialization으로 통일하기
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class DefaultResponse<T>(
    @SerializedName("code")
    @SerialName("code")
    @JsonNames("resultCode")
    val resultCode: Int,
    @SerializedName("message")
    @SerialName("message")
    @JsonNames("resultMsg")
    val resultMsg: String,
    @Contextual val data: T? = null,
) {
    val isSuccess: Boolean
        get() = (resultCode == 200)
}