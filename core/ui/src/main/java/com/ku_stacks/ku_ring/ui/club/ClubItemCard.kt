package com.ku_stacks.ku_ring.ui.club

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_kuring_icon_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_star_fill_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_star_v2
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.ensureLineHeight
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.RecruitmentStatus
import com.ku_stacks.ku_ring.domain.calculateDDay
import com.ku_stacks.ku_ring.domain.getRecruitmentStatus
import com.ku_stacks.ku_ring.ui.club.preview.ClubItemCardPreviewParameterProvider
import com.ku_stacks.ku_ring.util.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClubItemCard(
    clubSummary: ClubSummary,
    onSubscribeToggleClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    today: LocalDate = LocalDate.now(),
) {
    val recruitmentStatus = clubSummary.getRecruitmentStatus(LocalDateTime.now())
    val isRecruitmentCompleted = recruitmentStatus == RecruitmentStatus.CLOSED
    val containerColor = if (isRecruitmentCompleted) {
        KuringTheme.colors.gray200
    } else {
        KuringTheme.colors.background
    }

    // 동아리 태그는 동아리 카테고리와 동아리 소속을 포함
    val tags = listOf(
        clubSummary.category.koreanName,
        clubSummary.division.koreanName,
    )
    val dDay = clubSummary.calculateDDay(today) ?: 0

    Surface(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = KuringTheme.colors.gray100,
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 1.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailImage(
                clubName = clubSummary.name,
                logoUrl = clubSummary.posterImageUrl,
                isRecruitmentCompleted = isRecruitmentCompleted,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(3f / 4f)
                    .padding(bottom = 9.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = clubSummary.name,
                        style = KuringTheme.typography.body1.ensureLineHeight(),
                        color = KuringTheme.colors.textTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (!isRecruitmentCompleted) {
                        ClubDeadlineTag(
                            dDay = dDay,
                            recruitmentStatus = recruitmentStatus
                        )
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = clubSummary.summary,
                    style = KuringTheme.typography.caption1.ensureLineHeight(),
                    color = KuringTheme.colors.textCaption2,
                    minLines = 2,   // 텍스트가 짧아도 2줄을 보장
                    maxLines = 2,   // 텍스트가 길어도 2줄을 보장
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(7.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        tags.forEach { tag -> ClubTag(tag) }
                    }
                    SubscribeToggle(
                        isSubscribed = clubSummary.isSubscribed,
                        subscribeCount = clubSummary.subscribeCount,
                        onToggle = onSubscribeToggleClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun ThumbnailImage(
    clubName: String,
    logoUrl: String?,
    isRecruitmentCompleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val (containerColor, contentColor) = if (isRecruitmentCompleted) {
        KuringTheme.colors.gray200 to KuringTheme.colors.gray100
    } else {
        KuringTheme.colors.gray100 to KuringTheme.colors.gray200
    }

    Box(
        modifier = modifier
            .background(color = containerColor, shape = RoundedCornerShape(14))
            .clip(RoundedCornerShape(14)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = ic_kuring_icon_v2),
            contentDescription = null,
            tint = contentColor,
        )

        AsyncImage(
            model = logoUrl,
            contentDescription = clubName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun SubscribeToggle(
    isSubscribed: Boolean,
    subscribeCount: Int,
    modifier: Modifier = Modifier,
    onToggle: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .defaultMinSize(40.dp, 40.dp) // 터치 영역 확보
            .toggleable(
                value = isSubscribed,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = { onToggle(it) },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = subscribeCount.toString(),
            style = KuringTheme.typography.tag2.ensureLineHeight(),
            color = KuringTheme.colors.textCaption1,
            modifier = Modifier.padding(start = 2.dp),
        )

        val iconRes = if (isSubscribed) ic_star_fill_v2 else ic_star_v2
        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun ClubItemCardWhenRecruitmentOnGoingPreview(
    @PreviewParameter(ClubItemCardPreviewParameterProvider::class) clubSummary: ClubSummary,
) {
    KuringTheme {
        ClubItemCard(
            clubSummary = clubSummary,
            onSubscribeToggleClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}
