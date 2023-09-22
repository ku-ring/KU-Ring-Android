package com.ku_stacks.ku_ring.notice.api.response

import com.google.gson.annotations.SerializedName

data class SubscribeListResponse(
    @SerializedName(value = "message")
    val resultMsg: String,
    @SerializedName(value = "code")
    val resultCode: Int,
    @SerializedName(value = "data")
    val categoryList: List<CategoryResponse>
) {
    val isSuccess: Boolean
        get() = (resultCode == 200)

    companion object {
        fun mock() = SubscribeListResponse(
            resultMsg = "성공",
            resultCode = 200,
            categoryList = listOf(
                CategoryResponse("student", "stu", "학생"),
                CategoryResponse("employment", "emp", "취창업"),
            )
        )
    }
}