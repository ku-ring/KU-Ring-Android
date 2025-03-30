package com.ku_stacks.ku_ring.designsystem.components.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.PlainNoticeComment

@Composable
fun Comment(
    comment: NoticeComment,
    onReplyIconClick: () -> Unit,
    onDeleteComment: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(KuringTheme.colors.background),
    ) {
        Comment(
            comment = comment.comment,
            onReplyIconClick = onReplyIconClick,
            onDeleteComment = onDeleteComment,
            modifier = Modifier.fillMaxWidth(),
        )
        comment.replies.forEach { reply ->
            Row(modifier = Modifier.padding(top = 6.dp, end = 7.dp, bottom = 6.dp)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_reply_v2),
                    contentDescription = null,
                    tint = KuringTheme.colors.gray300,
                    modifier = Modifier
                        .padding(start = 7.dp, top = 16.dp, end = 3.dp)
                        .size(24.dp),
                )
                Comment(
                    comment = reply,
                    onReplyIconClick = onReplyIconClick,
                    onDeleteComment = onDeleteComment,
                    modifier = Modifier.background(
                        color = KuringTheme.colors.gray100,
                        shape = RoundedCornerShape(20.dp),
                    ),
                    contentPadding = PaddingValues(horizontal = 17.dp, vertical = 9.dp),
                )
            }
        }
    }
}

@Composable
fun Comment(
    comment: PlainNoticeComment,
    onReplyIconClick: () -> Unit,
    onDeleteComment: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        CommentHeader(
            commentId = comment.id,
            username = comment.authorName,
            onReplyIconClick = onReplyIconClick,
            onDeleteComment = onDeleteComment,
        )
        Text(
            text = comment.content,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.gray400,
            )
        )
        Text(
            text = comment.postedDatetime,
            style = TextStyle(
                fontSize = 10.sp,
                lineHeight = 16.3.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption1,
            )
        )
    }
}

@Composable
private fun CommentHeader(
    commentId: Int,
    username: String,
    onReplyIconClick: () -> Unit,
    onDeleteComment: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_user_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
        )
        Text(
            text = username,
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.45.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textBody,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_message_circle_v2),
            contentDescription = stringResource(R.string.comment_reply_icon),
            modifier = Modifier.clickable { onReplyIconClick() },
            tint = KuringTheme.colors.textBody,
        )
        // TODO: 자기 댓글에만 보여주기?
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_trashcan_v2),
            contentDescription = stringResource(R.string.comment_delete),
            modifier = Modifier.clickable { onDeleteComment(commentId) },
            tint = KuringTheme.colors.textBody,
        )
    }
}

private val previewComment = PlainNoticeComment(
    id = 0,
    parentNoticeId = 0,
    authorId = 0,
    authorName = "쿠링",
    noticeId = 0,
    content = "쿠링 댓글 내용".repeat(10),
    postedDatetime = "2025.03.23 20:27",
    updatedDatetime = "2025.03.23 20:27",
)

@LightAndDarkPreview
@Composable
private fun CommentPreview() {
    KuringTheme {
        Comment(
            comment = NoticeComment(
                comment = previewComment,
                replies = List(3) { previewComment },
                hasNext = false,
            ),
            onReplyIconClick = {},
            onDeleteComment = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}