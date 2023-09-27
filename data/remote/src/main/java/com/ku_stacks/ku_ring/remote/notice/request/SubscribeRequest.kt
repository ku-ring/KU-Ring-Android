package com.ku_stacks.ku_ring.remote.notice.request

import com.google.gson.annotations.SerializedName

data class SubscribeRequest(
    @SerializedName(value = "categories")
    val categories: List<String>
) {
    companion object {
        fun mock() = SubscribeRequest(
            categories = listOf("bachelor", "scholarship")
        )
    }
}