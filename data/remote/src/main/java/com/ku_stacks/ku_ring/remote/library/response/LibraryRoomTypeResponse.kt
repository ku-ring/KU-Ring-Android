package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomTypeResponse(
    val id : Int,
    @SerializedName("name") val roomName: String,
    val sortOrder: Int
)
