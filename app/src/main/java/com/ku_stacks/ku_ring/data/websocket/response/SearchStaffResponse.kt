package com.ku_stacks.ku_ring.data.websocket.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchStaffResponse(
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "major")
    val major: String,
    @SerializedName(value = "lab")
    val lab: String,
    @SerializedName(value = "phone")
    val phone: String,
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "deptName")
    val department: String,
    @SerializedName(value = "collegeName")
    val college: String
) : Serializable
