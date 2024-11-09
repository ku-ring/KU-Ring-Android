package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomBranchResponse (
    val id: Int,
    @SerializedName("name") val roomBranchName: String,
    val alias: String,
    val libraryCode: String,
    val sortOrder: Int
)