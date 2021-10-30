package com.ku_stacks.ku_ring.data.entity

import com.google.gson.annotations.SerializedName

data class Subscribe(
    @SerializedName(value = "id")
    val token: String,
    @SerializedName(value = "categories")
    val categories: List<String>
)