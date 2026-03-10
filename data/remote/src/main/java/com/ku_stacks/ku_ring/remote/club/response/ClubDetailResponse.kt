package com.ku_stacks.ku_ring.remote.club.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubDetailResponse(
    val id: Int,
    val name: String,
    @SerialName("summary")
    val shortIntroduction: String,
    val category: String,
    val division: String,
    @SerialName("subscriberCount")
    val subscriptionCount: Int,
    val isSubscribed: Boolean,
    val instagramUrl: String?,
    val youtubeUrl: String?,
    val etcUrl: String?,
    val description: String?,
    val qualifications: String?,
    val recruitmentStatus: String,
    val recruitStartAt: String?,
    val recruitEndAt: String?,
    val applyUrl: String?,
    @SerialName("posterImageUrl")
    val descriptionImageUrl: String?,
    val location: ClubRoomLocation,
)

@Serializable
data class ClubRoomLocation(
    @SerialName("building")
    val buildingName: String,
    val room: String,
    @SerialName("lon")
    val longitude: Double?,
    @SerialName("lat")
    val latitude: Double?,
)
