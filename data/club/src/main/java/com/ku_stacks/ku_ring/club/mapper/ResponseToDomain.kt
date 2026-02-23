package com.ku_stacks.ku_ring.club.mapper

import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubAffiliation
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubLocation
import com.ku_stacks.ku_ring.domain.ClubRecruitment
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.RecruitmentStatus
import com.ku_stacks.ku_ring.remote.club.response.ClubDetailResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubListItem
import com.ku_stacks.ku_ring.remote.club.response.ClubRoomLocation
import kotlinx.datetime.LocalDateTime

fun ClubDetailResponse.toClub(): Club {
    return Club(
        id = id,
        name = name,
        summary = shortIntroduction,
        category = category.uppercase().toEnumOrDefault<ClubCategory>(ClubCategory.OTHERS),
        affiliation = affiliation.uppercase()
            .toEnumOrDefault<ClubAffiliation>(ClubAffiliation.OTHERS),
        division = division.uppercase().toEnumOrDefault<ClubDivision>(ClubDivision.ETC),
        description = description,
        location = location.toLocation(),
        applyQualification = qualifications,
        recruitment = parseRecruitment(),
        webUrl = null,
        posterImageUrl = posterImageUrl,
        descriptionImageUrl = if (posterImageUrl.isEmpty()) null else listOf(posterImageUrl),
        isSubscribed = isSubscribed,
        subscribeCount = subscriptionCount,
    )
}

fun ClubRoomLocation.toLocation() = ClubLocation(
    building = buildingName,
    roomNumber = room,
    // TODO: API 문서 변경 완료되면 위도 및 경도 추가
    latitude = null,
    longitude = null,
)

fun ClubDetailResponse.parseRecruitment(): ClubRecruitment? {
    return if (recruitStartAt.isNotEmpty() && recruitEndAt.isNotEmpty()) {
        val start = LocalDateTime.parse(recruitStartAt)
        val end = LocalDateTime.parse(recruitEndAt)
        ClubRecruitment(
            start = start,
            end = end,
            recruitmentStatus = recruitmentStatus.uppercase()
                .toEnumOrDefault<RecruitmentStatus>(RecruitmentStatus.BEFORE),
            applyLink = applyUrl,
        )
    } else {
        null
    }
}

fun ClubListItem.toClubSummary(): ClubSummary {
    val start = recruitStartAt.toLocalDateTimeOrNull()
    val end = recruitEndAt.toLocalDateTimeOrNull()

    return ClubSummary(
        id = id,
        name = name,
        summary = shortIntroduction,
        category = category.uppercase().toEnumOrDefault<ClubCategory>(ClubCategory.OTHERS),
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

private fun String.toLocalDateTimeOrNull(): LocalDateTime? {
    return try {
        if (this.isEmpty()) null else LocalDateTime.parse(this)
    } catch (e: Exception) {
        null
    }
}
