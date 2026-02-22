package com.ku_stacks.ku_ring.ui.club

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubAffiliation
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubLocation
import com.ku_stacks.ku_ring.domain.ClubRecruitment
import com.ku_stacks.ku_ring.domain.RecruitmentStatus

class ClubsPreviewParameterProvider : PreviewParameterProvider<List<Club>> {
    val dummyClubs = listOf(
        Club(
            id = 1,
            name = "쿠링 학술 소모임",
            summary = "안드로이드를 공부하는 모임입니다.",
            category = ClubCategory.ACADEMIC,
            affiliation = ClubAffiliation.CENTRAL,
            division = ClubDivision.ENGINEERING,
            description = "상세 설명",
            location = ClubLocation("공학관", "A101", null, null),
            applyQualification = "컴퓨터공학과 학생",
            recruitment = ClubRecruitment(null, null, RecruitmentStatus.RECRUITING, null),
            webUrl = null,
            posterImageUrl = null,
            descriptionImageUrl = null,
            isSubscribed = false,
            subscribeCount = 100
        ),
        Club(
            id = 2,
            name = "농구부",
            summary = "즐겁게 농구합시다.",
            category = ClubCategory.ACTIVITIES,
            affiliation = ClubAffiliation.CENTRAL,
            division = ClubDivision.CENTRAL,
            description = "상세 설명",
            location = null,
            applyQualification = null,
            recruitment = null,
            webUrl = null,
            posterImageUrl = null,
            descriptionImageUrl = null,
            isSubscribed = true,
            subscribeCount = 50
        )
    )

    override val values: Sequence<List<Club>>
        get() = sequenceOf(
            dummyClubs,
            emptyList(),
        )
}
