package com.ku_stacks.ku_ring.data.websocket.response

import com.google.gson.annotations.SerializedName

data class StaffListResponse(
    @SerializedName(value = "isSuccess")
    val isSuccess: Boolean,
    @SerializedName(value = "resultMsg")
    val resultMsg: String,
    @SerializedName(value = "resultCode")
    val resultCode: Int,
    @SerializedName(value = "staffList")
    val staffList: List<StaffResponse>
)
