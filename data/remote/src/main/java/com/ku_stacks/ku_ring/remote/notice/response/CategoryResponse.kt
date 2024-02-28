package com.ku_stacks.ku_ring.remote.notice.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("name") val name: String,
    @SerializedName("hostPrefix") val shortName: String,
    @SerializedName("korName") val koreanName: String,
)
