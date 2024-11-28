package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomBranchResponse (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val roomBranchName: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("libraryCode")
    val libraryCode: String,
    @SerializedName("sortOrder")
    val sortOrder: Int,
)