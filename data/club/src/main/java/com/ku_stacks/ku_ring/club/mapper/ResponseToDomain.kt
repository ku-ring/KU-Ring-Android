package com.ku_stacks.ku_ring.club.mapper

import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubAffiliation
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubLocation
import com.ku_stacks.ku_ring.domain.ClubRecruitment
import com.ku_stacks.ku_ring.domain.RecruitmentStatus
import com.ku_stacks.ku_ring.remote.club.response.ClubDetailResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubRoomLocation
import kotlinx.datetime.LocalDateTime

fun ClubDetailResponse.toClub(): Club {
    return Club(
        id = id,
        name = name,
        summary = shortIntroduction,
        category = findEnumValue<ClubCategory>(category.uppercase(), ClubCategory.OTHERS),
        affiliation = findEnumValue<ClubAffiliation>(
            affiliation.uppercase(),
            ClubAffiliation.OTHERS
        ),
        division = findEnumValue<ClubDivision>(division.uppercase(), ClubDivision.ETC),
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
            recruitmentStatus = findEnumValue<RecruitmentStatus>(
                recruitmentStatus.uppercase(),
                RecruitmentStatus.BEFORE
            ),
            applyLink = applyUrl,
        )
    } else {
        null
    }
}

private inline fun <reified T : Enum<T>> findEnumValue(
    value: String,
    default: T
): T {
    return try {
        enumValueOf(value)
    } catch (e: IllegalArgumentException) {
        default
    }
}