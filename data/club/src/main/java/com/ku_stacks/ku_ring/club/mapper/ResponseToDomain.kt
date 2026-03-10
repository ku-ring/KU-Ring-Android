package com.ku_stacks.ku_ring.club.mapper

import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubLocation
import com.ku_stacks.ku_ring.domain.ClubRecruitment
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.RecruitmentStatus
import com.ku_stacks.ku_ring.remote.club.response.ClubDetailResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubListItem
import com.ku_stacks.ku_ring.remote.club.response.ClubRoomLocation
import com.ku_stacks.ku_ring.util.toLocalDateTimeOrNull

fun ClubDetailResponse.toClub(): Club {
    return Club(
        id = id,
        name = name,
        summary = shortIntroduction,
        category = category.uppercase().toEnumOrDefault<ClubCategory>(ClubCategory.ALL),
        division = division.uppercase().toEnumOrDefault<ClubDivision>(ClubDivision.ETC),
        description = description ?: "",
        location = location.toLocation(),
        applyQualification = qualifications,
        recruitment = parseRecruitment(),
        webUrl = listOfNotNull(instagramUrl, youtubeUrl, etcUrl),
        posterImageUrl = descriptionImageUrl,
        descriptionImageUrl = descriptionImageUrl?.let { listOf(it) },
        isSubscribed = isSubscribed,
        subscribeCount = subscriptionCount,
    )
}

fun ClubRoomLocation.toLocation() = ClubLocation(
    building = buildingName,
    roomNumber = room,
    latitude = latitude,
    longitude = longitude,
)

fun ClubDetailResponse.parseRecruitment(): ClubRecruitment? {
    val recruitStartDate = recruitStartAt?.toLocalDateTimeOrNull()
    val recruitEndDate = recruitEndAt?.toLocalDateTimeOrNull()
    val recruitmentStatus = recruitmentStatus

    return if (recruitStartDate != null && recruitEndDate != null) {
        ClubRecruitment(
            start = recruitStartDate,
            end = recruitEndDate,
            recruitmentStatus = recruitmentStatus.uppercase()
                .toEnumOrDefault<RecruitmentStatus>(RecruitmentStatus.BEFORE),
            applyLink = applyUrl,
        )
    } else {
        null
    }
}

fun ClubListItem.toClubSummary(): ClubSummary {
    val start = recruitStartDate?.toLocalDateTimeOrNull()
    val end = recruitEndDate?.toLocalDateTimeOrNull()

    return ClubSummary(
        id = id,
        name = name,
        summary = shortIntroduction,
        category = category.uppercase().toEnumOrDefault<ClubCategory>(ClubCategory.ALL),
        division = division.uppercase().toEnumOrDefault<ClubDivision>(ClubDivision.ETC),
        posterImageUrl = imageUrl,
        isSubscribed = isSubscribed,
        subscribeCount = subscriberCount,
        recruitmentStart = start,
        recruitmentEnd = end,
    )
}

private inline fun <reified T : Enum<T>> String.toEnumOrDefault(default: T): T {
    return try {
        enumValueOf(this)
    } catch (e: IllegalArgumentException) {
        default
    }
}
