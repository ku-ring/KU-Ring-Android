package com.ku_stacks.kustack.data.api.response

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName(value = "trackId")
    val trackId: Int,
    @SerializedName(value = "trackName")
    val trackName: String,
    @SerializedName(value = "collectionName")
    val collectionName: String,
    @SerializedName(value = "artistName")
    val artistName: String,
    @SerializedName(value = "artworkUrl60")
    val ImageUrl: String,

    var isFavorite: Boolean = false
)