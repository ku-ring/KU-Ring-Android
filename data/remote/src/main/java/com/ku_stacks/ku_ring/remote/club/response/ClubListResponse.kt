package com.ku_stacks.ku_ring.remote.club.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubListResponse(
    val clubs: List<ClubListItem>,
)

@Serializable
data class ClubListItem(
    val id: Int,
    val name: String,
    @SerialName("summary")
    val shortIntroduction: String,
    @SerialName("iconImageUrl")
    val imageUrl: String?,
    val category: String,
    val division: String,
    val isSubscribed: Boolean,
    val subscriberCount: Int,
    val recruitStartDate: String?,
    val recruitEndDate: String?,
)
