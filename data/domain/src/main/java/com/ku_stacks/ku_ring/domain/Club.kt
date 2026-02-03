package com.ku_stacks.ku_ring.domain

import kotlinx.datetime.LocalDateTime

/**
 * @property name 동아리 이름
 * @property affiliation 동아리 소속
 * @property division 동아리 분과
 * @property introduction 동아리 소개
 * @property applyRestriction 동아리 가입 조건 (없으면 `null`)
 * @property recruitment 동아리 모집 정보 (없으면 `null`)
 * @property clubRoom 동아리 방 위치 (없으면 `null`)
 * @property logoUrl 동아리 로고 URL (없으면 `null`)
 * @property webUrl 동아리 웹사이트 URL (없으면 `null`)
 */
data class Club(
    val name: String,
    val affiliation: ClubType,
    private val division: ClubDivision,
    val introduction: String,
    val applyRestriction: String?,
    val recruitment: ClubRecruitment?,
    val clubRoom: String?,
    val logoUrl: String?,
    val webUrl: String?,
    val isSubscribed: Boolean,
) {
    val category: ClubCategory = ClubCategory.ACADEMIC // TODO: convert division into category

    val clubRoomBuildingCode: Int =
        0 // TODO: extract building code from club room (or get from server)
}

enum class ClubType {
    CENTRAL,            // 중앙동아리
    COLLEGE,            // 단과대
    OTHERS,             // 기타
}

/**
 * 동아리 분과
 */
enum class ClubDivision {
    /**
     * 자연과학분과
     */
    NATURAL_SCIENCE,

    /**
     * 인문학술분과
     */
    HUMANITIES,

    /**
     * 봉사분과
     */
    VOLUNTEER,

    /**
     * 사회분과
     */
    SOCIAL_AFFAIRS,

    /**
     * 종교분과
     */
    RELIGION,

    /**
     * 전시문예분과
     */
    EXHIBITION_ARTS,

    /**
     * 공연예술분과
     */
    PERFORMING_ARTS,

    /**
     * 레저무예분과
     */
    LEISURE_MARTIAL,

    /**
     * 구기체육분과
     */
    SPORTS,

    /**
     * 기타
     */
    OTHERS,
}

/**
 * 쿠링에서 분류하는 4가지 카테고리
 */
enum class ClubCategory {
    /**
     * 학술활동
     */
    ACADEMIC,

    /**
     * 문화예술
     */
    CULTURE_ARTS,

    /**
     * 사회가치
     */
    SOCIAL,

    /**
     * 야외활동
     */
    ACTIVITIES,

    /**
     * 기타 (예외 처리용)
     */
    OTHERS,
}

/**
 * @property content 모집 글 본문
 * @property start 모집 시작일 (없으면 `null`)
 * @property end 모집 마감일 (없으면 `null`. `start`와 `end` 둘 다 `null`일 경우 상시모집)
 * @property applyLink 가입 링크 (없으면 `null`)
 */
data class ClubRecruitment(
    val content: String,
    val start: LocalDateTime?,
    val end: LocalDateTime?,
    val applyLink: String?,
)