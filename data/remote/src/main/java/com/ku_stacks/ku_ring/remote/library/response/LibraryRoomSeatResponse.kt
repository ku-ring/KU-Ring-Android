package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomSeatResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("occupied")
    val occupied: Int,
    @SerializedName("waiting")
    val waiting: Int,
    @SerializedName("available")
    val available: Int,
)
