package com.ku_stacks.ku_ring.data.api.response

import com.google.gson.annotations.SerializedName

data class SearchStaffListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SearchStaffDataResponse,
) {
    val isSuccess: Boolean
        get() = (code == 200)
}