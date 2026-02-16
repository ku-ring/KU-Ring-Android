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
    val affiliation: String,
    val division: String,
    // TODO: bookmarkCount를 이걸로 통일 요청
    @SerialName("subscriberCount")
    val subscriptionCount: Int,
    // TODO: isBookmarked를 이걸로 통일 요청
    val isSubscribed: Boolean,
    // TODO: json key 이름을 snsUrl로 변경하도록 요청; 요청 결과에 따라 수정 예정
    @SerialName("homepageUrl")
    val snsUrl: List<String>,
    val description: String,
    val qualifications: String,
    val recruitmentStatus: String,
    val isRecruiting: Boolean,
    val recruitStartAt: String,
    val recruitEndAt: String,
    val applyUrl: String,
    // TODO: 이거 또는 descriptionImageUrl 하나로 통일 요청
    val posterImageUrl: String,
    val location: ClubRoomLocation,
)

@Serializable
data class ClubRoomLocation(
    @SerialName("building")
    val buildingName: String,
    val room: String,
    // TODO: 건물 좌표 추가 요청 (예시 json에는 있지만 문서에는 없는 상태)
)