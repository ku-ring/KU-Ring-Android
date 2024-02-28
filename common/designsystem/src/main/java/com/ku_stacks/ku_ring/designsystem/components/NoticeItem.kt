package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.SfProDisplay
import com.ku_stacks.ku_ring.domain.Notice

@Composable
fun NoticeItem(
    notice: Notice,
    modifier: Modifier = Modifier,
    onClick: (Notice) -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
            .clickable { onClick(notice) }
            .fillMaxWidth()
    ) {
        val (point, subjectText, postedDateText, tags) = createRefs()
        NoticeItemPoint(
            isNew = notice.isNew,
            isRead = notice.isRead,
            isSubscribing = notice.isSubscribing,
            isSaved = notice.isSaved,
            modifier = Modifier.constrainAs(point) {
                start.linkTo(parent.start, margin = 20.dp)
                top.linkTo(parent.top, margin = 26.dp)
                bottom.linkTo(parent.bottom, margin = 34.dp)
            }
        )
        NoticeItemSubject(
            subject = notice.subject,
            isRead = notice.isRead,
            modifier = Modifier
                .constrainAs(subjectText) {
                    start.linkTo(point.end, margin = 12.dp)
                    end.linkTo(parent.end, margin = 22.dp)
                    top.linkTo(parent.top, 7.dp)
                    width = Dimension.fillToConstraints
                }
        )
        NoticeItemPostedDate(
            postedDate = notice.postedDate,
            isRead = notice.isRead,
            modifier = Modifier.constrainAs(postedDateText) {
                start.linkTo(subjectText.start)
                top.linkTo(subjectText.bottom, margin = 6.dp)
                bottom.linkTo(parent.bottom, margin = 20.dp)
            },
        )
        NoticeItemTags(
            tags = notice.tag,
            modifier = Modifier.constrainAs(tags) {
                start.linkTo(postedDateText.end, margin = 8.dp)
                top.linkTo(postedDateText.top)
            }
        )
    }
}

@Composable
private fun NoticeItemPoint(
    isNew: Boolean,
    isRead: Boolean,
    isSubscribing: Boolean,
    isSaved: Boolean,
    modifier: Modifier = Modifier,
) {
    val pointColorId = when {
        isSaved -> R.color.kus_green// green
        isSubscribing -> R.color.kus_pink //pink
        else -> R.color.kus_secondary_gray// gray
    }
    val pointColor = colorResource(id = pointColorId)

    // visibility 대용
    val alpha = when {
        isSaved -> 1f
        isRead -> 0f
        isNew -> 1f
        else -> 0f
    }

    val pointDrawable = ResourcesCompat.getDrawable(
        LocalContext.current.resources,
        R.drawable.point_primary_gray,
        null
    ) ?: return
    val size = with(LocalDensity.current) { 8.dp.roundToPx() }
    Image(
        bitmap = pointDrawable.toBitmap(size, size).asImageBitmap(),
        contentDescription = null,
        colorFilter = ColorFilter.tint(pointColor),
        modifier = modifier.alpha(alpha)
    )
}

@Composable
private fun NoticeItemSubject(
    subject: String,
    isRead: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = subject,
        modifier = modifier,
        fontFamily = SfProDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = colorResource(id = if (isRead) R.color.kus_secondary_label else R.color.kus_label)
    )
}

@Composable
private fun NoticeItemPostedDate(
    postedDate: String,
    isRead: Boolean,
    modifier: Modifier = Modifier,
) {
    val dottedPostedDate = if (postedDate.length == 8) {
        "${postedDate.substring(0..3)}.${postedDate.substring(4..5)}.${postedDate.substring(6..7)}"
    } else {
        postedDate
    }
    Text(
        text = dottedPostedDate,
        modifier = modifier,
        fontFamily = SfProDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colorResource(id = if (isRead) R.color.kus_secondary_label else R.color.kus_label)
    )
}

@Composable
private fun NoticeItemTags(
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        tags.forEach { tag ->
            NoticeItemTag(
                tag = tag,
                modifier = Modifier
                    .clip(RoundedCornerShape(11.dp))
                    .background(colorResource(R.color.kus_secondary_gray))
            )
        }
    }
}

@Composable
private fun NoticeItemTag(
    tag: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = tag,
        modifier = modifier.padding(horizontal = 4.dp, vertical = 0.5.dp),
        fontFamily = SfProDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = colorResource(R.color.kus_background)
    )
}

@LightAndDarkPreview
@Composable
private fun NoticeItemSubjectPreview() {
    val subject = "2021-2학기 중간고사 이후 수업 운영 가이드라인"
    KuringTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            NoticeItemSubject(
                subject = subject,
                isRead = false,
            )
            NoticeItemSubject(
                subject = subject,
                isRead = true,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeItemPostedDatePreview() {
    KuringTheme {
        NoticeItemPostedDate(
            postedDate = "2021.10.01",
            isRead = false,
            modifier = Modifier.padding(10.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeItemTagsPreview() {
    val tags = listOf("지급", "학사", "한국교육과정평가원")
    KuringTheme {
        NoticeItemTags(
            tags = tags,
            modifier = Modifier.padding(10.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeItemPreview() {
    val notice = Notice(
        postedDate = "2023.05.13",
        subject = "2023학년도 1학기 2차 교직 응급처치 및 심폐소생술 실습 안내",
        category = "department",
        department = "cse",
        url = "",
        articleId = "",
        isNew = false,
        isRead = false,
        isSubscribing = false,
        isSaved = false,
        isReadOnStorage = false,
        tag = listOf("지급"),
    )
    KuringTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            NoticeItem(notice = notice)
            NoticeItem(notice = notice.copy(isRead = true))
            NoticeItem(notice = notice.copy(isNew = true))
            NoticeItem(notice = notice.copy(isNew = true, isSubscribing = true))
            NoticeItem(notice = notice.copy(isSaved = true))
        }
    }
}