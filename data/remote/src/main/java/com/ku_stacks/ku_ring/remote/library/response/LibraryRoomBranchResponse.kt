package com.ku_stacks.ku_ring.remote.library.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryRoomBranchResponse (
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val roomBranchName: String,
    @SerialName("alias")
    val alias: String,
    @SerialName("libraryCode")
    val libraryCode: String,
    @SerialName("sortOrder")
    val sortOrder: Int,
)