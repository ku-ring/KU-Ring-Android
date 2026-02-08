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
import com.ku_stacks.ku_ring.ui.R.string.club_card_tag_d_day
import com.ku_stacks.ku_ring.ui.R.string.club_card_tag_recruitment_complete

private const val DEADLINE_THRESHOLD = 3

@Composable
fun ClubDeadlineTag(
    dDay: Int,
    isRecruitmentCompleted: Boolean,
) {
    val isNearDeadline = dDay < DEADLINE_THRESHOLD
    val text =
        if (isRecruitmentCompleted) stringResource(club_card_tag_recruitment_complete)
        else stringResource(club_card_tag_d_day, dDay.toString())
    val (containerColor, contentColor) = with(KuringTheme.colors) {
        when {
            isRecruitmentCompleted -> gray100 to textCaption2
            isNearDeadline -> event to red
            else -> gray100 to textCaption2
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
    containerColor: Color = KuringTheme.colors.textCaption2,
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
                isRecruitmentCompleted = false,
            )
            ClubDeadlineTag(
                dDay = 2,
                isRecruitmentCompleted = false,
            )
            ClubDeadlineTag(
                dDay = 0,
                isRecruitmentCompleted = true,
            )
        }
    }
}
