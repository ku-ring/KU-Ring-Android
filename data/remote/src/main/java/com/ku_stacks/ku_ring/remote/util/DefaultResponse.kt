package com.ku_stacks.ku_ring.remote.util

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("code") val resultCode: Int,
    @SerializedName("message") val resultMsg: String,
    val data: Any?,
) {
    val isSuccess: Boolean
        get() = (resultCode == 200)

    companion object {
        fun mock() = DefaultResponse(
            resultCode = 200,
            resultMsg = "성공",
            data = null,
        )
    }
}