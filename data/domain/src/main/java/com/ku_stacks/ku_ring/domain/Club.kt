package com.ku_stacks.ku_ring.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.daysUntil

/**
 * @property id 동아리 ID
 * @property name 동아리 이름
 * @property summary 동아리 한 줄 소개
 * @property category 동아리 카테고리
 * @property affiliation 동아리 소속
 * @property division 동아리 분과
 * @property description 동아리 상세 소개글
 * @property location 동아리 방 위치 (없으면 `null`)
 * @property applyQualification 동아리 가입 조건 (없으면 `null`)
 * @property recruitment 동아리 모집 정보 (없으면 `null`)
 * @property webUrl 동아리 웹사이트 URL (없으면 `null`)
 * @property posterImageUrl 동아리 포스터 URL (없으면 `null`)
 * @property descriptionImageUrl 동아리 소개 본문에 포함될 홍보 이미지/포스터 URL 목록 (없으면 `null`)
 * @property isSubscribed 동아리 구독 여부
 * @property subscribeCount 동아리 구독자 수
 */
data class Club(
    val id: Int,
    val name: String,
    val summary: String,
    val category: ClubCategory,
    val affiliation: ClubAffiliation,
    val division: ClubDivision,
    val description: String,
    val location: ClubLocation?,
    val applyQualification: String?,
    val recruitment: ClubRecruitment?,
    val webUrl: String?,
    val posterImageUrl: String?,
    val descriptionImageUrl: List<String>?,
    val isSubscribed: Boolean,
    val subscribeCount: Int,
)

/**
 * 쿠링에서 분류하는 5가지 동아리 카테고리
 */
enum class ClubCategory(
    val koreanName: String,
) {
    ACADEMIC("학술활동"),
    CULTURE_ARTS("문화예술"),
    SOCIAL("사회가치"),
    ACTIVITIES("야외활동"),
    OTHERS("기타"),
}

/**
 * 동아리 소속 대분류
 */
enum class ClubAffiliation {
    CENTRAL,            // 중앙동아리
    COLLEGE,            // 단과대
    OTHERS,             // 기타
}

/**
 * 동아리 소속 소분류
 */
enum class ClubDivision(
    val koreanName: String,
) {
    CENTRAL("중앙동아리"),
    LIBERAL_ARTS("문과대학"),
    SCIENCE("이과대학"),
    ARCHITECTURE("건축대학"),
    ENGINEERING("공과대학"),
    SOCIAL_SCIENCES("사회과학대학"),
    BUSINESS("경영대학"),
    REAL_ESTATE("부동산과학원"),
    KU_CONVERGENCE("KU융합과학기술원"),
    SANGHUH_LIFE_SCIENCE("상허생명과학대학"),
    VETERINARY("수의과대학"),
    ART_DESIGN("예술디자인대학"),
    EDUCATION("사범대학"),
    SANGHUH_GENERAL("상허교양대학"),
    INTERNATIONAL("국제대학"),
    CONVERGENCE_SCI_TECH("융합과학기술원"),
    LIFE_SCIENCE("생명과학대학"),
    ETC("기타"),
}

/**
 * @property start 모집 시작일 (없으면 `null`)
 * @property end 모집 마감일 (없으면 `null`)
 * @property recruitmentStatus 모집 상태
 * @property applyLink 가입 링크 (없으면 `null`)
 */
data class ClubRecruitment(
    val start: LocalDateTime?,
    val end: LocalDateTime?,
    val recruitmentStatus: RecruitmentStatus,
    val applyLink: String?,
)

/**
 * 동아리 모집 여부를 나타내는 카테고리
 */
enum class RecruitmentStatus {
    BEFORE,
    RECRUITING,
    CLOSED,
    ALWAYS,
}

/**
 * @property building 동아리 방 위치 건물
 * @property roomNumber 동아리 방 위치 방 번호
 * @property latitude 동아리 방 위치 위도 (없으면 `null`)
 * @property longitude 동아리 방 위치 경도 (없으면 `null`)
 */
data class ClubLocation(
    val building: String,
    val roomNumber: String,
    val latitude: Double?,
    val longitude: Double?,
)

fun Club.calculateDDay(today: LocalDate): Int? =
    recruitment?.end?.date?.let { endDate ->
        today.daysUntil(endDate)
    }
