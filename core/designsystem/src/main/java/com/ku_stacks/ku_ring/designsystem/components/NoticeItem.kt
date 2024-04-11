package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Notice

/**
 * 공지를 보여주는 컴포넌트이다.
 *
 * @param notice 보여줄 공지 객체
 * @param modifier 적용할 [Modifier]
 * @param onClick 공지를 클릭했을 때의 콜백
 * @param contentVerticalAlignment [content]의 수직 정렬 위치이다. 북마크 아이콘은 이 값과 상관없이 항상 [Alignment.Top]으로 정렬된다.
 * @param content 컴포넌트 오른쪽에 보여줄 slot이다. [content]가 주어지면 북마크 아이콘이 보이지 않는다.
 */
@Composable
fun NoticeItem(
    notice: Notice,
    modifier: Modifier = Modifier,
    onClick: (Notice) -> Unit = {},
    contentVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable (() -> Unit)? = null,
) {
    val background =
        if (notice.isImportant) KuringTheme.colors.mainPrimarySelected else KuringTheme.colors.background
    Row(
        modifier = modifier
            .clickable { onClick(notice) }
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 20.dp),
        verticalAlignment = contentVerticalAlignment,
    ) {
        NoticeItemContent(
            notice = notice,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f),
        )
        if (content != null) {
            content()
        } else if (notice.isSaved) {
            NoticeItemBookmarkIcon(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .align(Alignment.Top),
            )
        }
    }
}

@Composable
private fun NoticeItemContent(
    notice: Notice,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (notice.isImportant) {
                NoticeItemImportantTag()
            }
            NoticeItemTitle(
                title = notice.subject,
                isRead = notice.isRead,
            )
            NoticeItemDate(
                date = notice.postedDate,
                isRead = notice.isRead,
            )
        }
    }
}

@Composable
private fun NoticeItemImportantTag(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(50)
    Text(
        text = stringResource(id = R.string.notice_item_important_tag),
        style = TextStyle(
            fontSize = 11.sp,
            lineHeight = 17.93.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(600),
            color = KuringTheme.colors.mainPrimary,
        ),
        modifier = modifier
            .background(color = Color.White, shape = shape)
            .border(width = 0.5.dp, color = KuringTheme.colors.mainPrimary, shape = shape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    )
}

@Composable
private fun NoticeItemTitle(
    title: String,
    isRead: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (isRead) KuringTheme.colors.textCaption1 else KuringTheme.colors.textBody
    Text(
        text = title,
        style = TextStyle(
            fontFamily = Pretendard,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(500),
            color = color,
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Composable
private fun NoticeItemDate(
    date: String,
    isRead: Boolean,
    modifier: Modifier = Modifier,
) {
    // 현재 디자인된 색깔은 다크 모드에서 너무 안 보임
    // 임시로 onSurface(alpha=0.8) 색을 사용
    val color = if (isRead) KuringTheme.colors.textCaption1 else KuringTheme.colors.textBody
    Text(
        text = date,
        style = TextStyle(
            fontFamily = Pretendard,
            fontSize = 14.sp,
            lineHeight = 22.82.sp,
            fontWeight = FontWeight(500),
            color = color,
        ),
        modifier = modifier,
    )
}

@Composable
private fun NoticeItemBookmarkIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_notice_bookmark_v2),
        contentDescription = null,
        modifier = modifier,
        tint = KuringTheme.colors.mainPrimary,
    )
}

private val notice = Notice(
    postedDate = "2023.05.13",
    subject = "코로나-19 재확산에 따른 방역 수칙 및 자발적 거리두기 중요 내용 안내",
    category = "department",
    department = "cse",
    url = "",
    articleId = "",
    isNew = false,
    isRead = false,
    isSubscribing = false,
    isSaved = false,
    isReadOnStorage = false,
    isImportant = false,
    tag = listOf("지급"),
)

@LightAndDarkPreview
@Composable
private fun NoticeItemPreview() {
    KuringTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(KuringTheme.colors.background),
        ) {
            NoticeItem(notice = notice)
            NoticeItem(notice = notice.copy(isImportant = true))
            NoticeItem(notice = notice.copy(isRead = true))
            NoticeItem(notice = notice.copy(isSaved = true))
        }
    }
}