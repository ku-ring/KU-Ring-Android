package com.ku_stacks.kustack.data.api.response

import com.google.gson.annotations.SerializedName

data class TrackListResponse(
    @SerializedName(value = "resultCount")
    val resultCount: Int,
    @SerializedName(value = "results")
    val results: ArrayList<TrackResponse>
)