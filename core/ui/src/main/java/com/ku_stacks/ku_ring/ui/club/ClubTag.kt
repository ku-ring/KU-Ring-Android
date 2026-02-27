package com.ku_stacks.ku_ring.ui.club

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.ensureLineHeight
import com.ku_stacks.ku_ring.domain.RecruitmentStatus
import com.ku_stacks.ku_ring.ui.R.string.club_card_tag_d_day
import com.ku_stacks.ku_ring.ui.R.string.club_card_tag_d_day_end
import com.ku_stacks.ku_ring.ui.R.string.club_card_tag_recruitment_always
import com.ku_stacks.ku_ring.ui.R.string.club_card_tag_recruitment_end

private const val DEADLINE_THRESHOLD = 3

@Composable
fun ClubDeadlineTag(
    dDay: Int,
    recruitmentStatus: RecruitmentStatus,
) {
    val isNearDeadline = dDay <= DEADLINE_THRESHOLD
    val text = when {
        recruitmentStatus == RecruitmentStatus.ALWAYS -> stringResource(
            club_card_tag_recruitment_always
        )
        recruitmentStatus == RecruitmentStatus.CLOSED -> stringResource(
            club_card_tag_recruitment_end
        )

        dDay == 0 -> stringResource(club_card_tag_d_day_end)
        else -> stringResource(club_card_tag_d_day, dDay.toString())
    }
    val (containerColor, contentColor) = with(KuringTheme.colors) {
        when {
            recruitmentStatus != RecruitmentStatus.ALWAYS && isNearDeadline -> event to red
            else -> gray100 to textCaption1
        }
    }

    ClubTag(
        text = text,
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

@Composable
fun ClubTag(
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color = KuringTheme.colors.gray100,
    containerColor: Color = KuringTheme.colors.textCaption1,
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            style = KuringTheme.typography.tag.ensureLineHeight(),
            color = contentColor,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubTagPreview() {
    KuringTheme {
        Column(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ClubDeadlineTag(
                dDay = 7,
                recruitmentStatus = RecruitmentStatus.RECRUITING,
            )
            ClubDeadlineTag(
                dDay = 2,
                recruitmentStatus = RecruitmentStatus.RECRUITING,
            )
            ClubDeadlineTag(
                dDay = 0,
                recruitmentStatus = RecruitmentStatus.RECRUITING,
            )
            ClubDeadlineTag(
                dDay = 0,
                recruitmentStatus = RecruitmentStatus.ALWAYS,
            )
        }
    }
}
