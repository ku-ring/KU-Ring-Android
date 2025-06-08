package com.ku_stacks.ku_ring.designsystem.components.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
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
    isReplyComment: Boolean,
    onReplyIconClick: () -> Unit,
    onDeleteComment: (Int) -> Unit,
    dropdownCommentId: Int?,
    onDropdownShow: (Int) -> Unit,
    onDismissDropdown: () -> Unit,
    onReport: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (isReplyComment) {
        KuringTheme.colors.mainPrimarySelected
    } else {
        Color.Transparent
    }

    Column(
        modifier = modifier.background(background),
    ) {
        Comment(
            comment = comment.comment,
            onReplyIconClick = onReplyIconClick,
            onDeleteComment = onDeleteComment,
            modifier = Modifier.fillMaxWidth(),
            isDropdownVisible = (dropdownCommentId == comment.comment.id),
            onDropdownShow = onDropdownShow,
            onDismissDropdown = onDismissDropdown,
            onReport = onReport,
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
                    isDropdownVisible = (dropdownCommentId == reply.id),
                    onDropdownShow = onDropdownShow,
                    onDismissDropdown = onDismissDropdown,
                    onReport = onReport,
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
    isDropdownVisible: Boolean,
    onDropdownShow: (Int) -> Unit,
    onDismissDropdown: () -> Unit,
    onReport: (Int) -> Unit,
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
            isReply = (comment.parentCommentId != null),
            onReplyIconClick = onReplyIconClick,
            isDeletable = comment.isMyComment,
            onDeleteComment = onDeleteComment,
            isDropdownVisible = isDropdownVisible,
            onDropdownShow = onDropdownShow,
            onDismissDropdown = onDismissDropdown,
            onReport = onReport,
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
    isReply: Boolean,
    onReplyIconClick: () -> Unit,
    isDeletable: Boolean,
    onDeleteComment: (Int) -> Unit,
    isDropdownVisible: Boolean,
    onDropdownShow: (Int) -> Unit,
    onDismissDropdown: () -> Unit,
    onReport: (Int) -> Unit,
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

        if (!isReply) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_message_circle_v2),
                contentDescription = stringResource(R.string.comment_reply_icon),
                modifier = Modifier.clickable(onClick = onReplyIconClick),
                tint = KuringTheme.colors.textBody,
            )
        }

        Box {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_more_vertical_v2),
                contentDescription = stringResource(R.string.comment_header_more),
                modifier = Modifier.clickable { onDropdownShow(commentId) },
                tint = KuringTheme.colors.textBody,
            )
            CommentDropdownMenu(
                isDropdownVisible = isDropdownVisible, // TODO
                onDismissDropdown = onDismissDropdown,// TODO
                onReport = { onReport(commentId) }, // TODO
                isDeletable = isDeletable,
                onDelete = { onDeleteComment(commentId) }, // TODO
            )
        }
    }
}

@Composable
private fun CommentDropdownMenu(
    isDropdownVisible: Boolean,
    onDismissDropdown: () -> Unit,
    onReport: () -> Unit,
    isDeletable: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 17.sp,
        fontFamily = Pretendard,
        fontWeight = FontWeight(400),
        color = KuringTheme.colors.textBody,
    )
    DropdownMenu(
        expanded = isDropdownVisible,
        onDismissRequest = onDismissDropdown,
        containerColor = KuringTheme.colors.background,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
    ) {
        val reportText = stringResource(R.string.comment_report)
        DropdownMenuItem(
            text = {
                Text(
                    text = reportText,
                    style = textStyle,
                )
            },
            onClick = onReport,
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_info_circle_mono_v2),
                    contentDescription = null,
                )
            },
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = reportText
            },
            colors = MenuDefaults.itemColors(
                textColor = KuringTheme.colors.textBody,
                trailingIconColor = KuringTheme.colors.textBody,
            ),
        )
        if (isDeletable) {
            val deleteText = stringResource(R.string.comment_delete)
            DropdownMenuItem(
                text = {
                    Text(
                        text = deleteText,
                        style = textStyle.copy(color = KuringTheme.colors.warning),
                    )
                },
                onClick = onDelete,
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_trashcan_v2),
                        contentDescription = null,
                    )
                },
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = deleteText
                },
                colors = MenuDefaults.itemColors(
                    textColor = KuringTheme.colors.warning,
                    trailingIconColor = KuringTheme.colors.warning,
                ),
            )
        }
    }
}

private val previewComment = PlainNoticeComment(
    id = 0,
    parentCommentId = null,
    authorId = 0,
    authorName = "쿠링",
    noticeId = 0,
    content = "쿠링 댓글 내용".repeat(10),
    isMyComment = true,
    postedDatetime = "2025.03.23 20:27",
    updatedDatetime = "2025.03.23 20:27",
)

@LightAndDarkPreview
@Composable
private fun CommentPreview() {
    var dropdownItemId by remember { mutableStateOf<Int?>(null) }
    KuringTheme {
        Comment(
            comment = NoticeComment(
                comment = previewComment,
                replies = List(3) {
                    previewComment.copy(
                        id = 10 + it,
                        parentCommentId = previewComment.id
                    )
                },
                hasNext = false,
            ),
            isReplyComment = true,
            onReplyIconClick = {},
            onDeleteComment = {},
            dropdownCommentId = dropdownItemId,
            onDropdownShow = { dropdownItemId = it },
            onDismissDropdown = { dropdownItemId = null },
            onReport = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}