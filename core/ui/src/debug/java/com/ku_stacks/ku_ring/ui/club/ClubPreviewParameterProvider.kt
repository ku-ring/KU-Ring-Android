package com.ku_stacks.ku_ring.ui.club

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubAffiliation
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubRecruitment
import com.ku_stacks.ku_ring.domain.RecruitmentStatus

class ClubItemCardPreviewParameterProvider : PreviewParameterProvider<Club> {
    val recruitment = ClubRecruitment(
        start = null,
        end = null,
        recruitmentStatus = RecruitmentStatus.RECRUITING,
        applyLink = null,
    )
    val club = Club(
        id = 1,
        name = "{ClubName}",
        category = ClubCategory.ACADEMIC,
        affiliation = ClubAffiliation.CENTRAL,
        division = ClubDivision.CENTRAL,
        summary = "{SubText}\n{SubText}",
        description = "{SubText}\n{SubText}",
        recruitment = recruitment,
        location = null,
        webUrl = emptyList(),
        applyQualification = null,
        posterImageUrl = null,
        descriptionImageUrl = null,
        isSubscribed = false,
        subscribeCount = 50,
    )

    override val values: Sequence<Club>
        get() = sequenceOf(
            club,
            club.copy(recruitment = recruitment.copy(recruitmentStatus = RecruitmentStatus.CLOSED))
        )
}
