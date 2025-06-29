package com.ku_stacks.ku_ring.notice_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.comment.Comment
import com.ku_stacks.ku_ring.designsystem.components.indicator.PagingLoadingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.notice_detail.R
import kotlinx.coroutines.flow.MutableStateFlow

// TODO: LazyNoticeItemColumn과 일반화

@Composable
fun LazyPagingCommentColumn(
    comments: LazyPagingItems<NoticeComment>,
    replyCommentId: Int?,
    setReplyCommentId: (Int?) -> Unit,
    onReportComment: (Int) -> Unit,
    onDeleteIconClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownCommentId by remember { mutableStateOf<Int?>(null) }
    LazyColumn(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (comments.itemCount == 0) {
            item {
                Text(
                    text = stringResource(R.string.comment_bottom_sheet_empty),
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.kus_label),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 100.dp)
                )
            }
        } else {
            commentItems(
                comments = comments,
                replyCommentId = replyCommentId,
                setReplyCommentId = setReplyCommentId,
                onDeleteIconClick = onDeleteIconClick,
                dropdownCommentId = dropdownCommentId,
                onDropdownShow = { dropdownCommentId = it },
                onDismissDropdown = { dropdownCommentId = null },
                onReport = onReportComment,
            )

            if (comments.loadState.append == LoadState.Loading) {
                item {
                    PagingLoadingIndicator(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .size(40.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun CommentErrorText(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.comment_bottom_sheet_error_loading),
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colorResource(id = R.color.kus_label),
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

private fun LazyListScope.commentItems(
    comments: LazyPagingItems<NoticeComment>,
    replyCommentId: Int?,
    setReplyCommentId: (Int?) -> Unit,
    onDeleteIconClick: (Int) -> Unit,
    dropdownCommentId: Int?,
    onDropdownShow: (Int) -> Unit,
    onDismissDropdown: () -> Unit,
    onReport: (Int) -> Unit,
) {
    items(
        count = comments.itemCount,
        key = comments.itemKey { it.comment.id },
        contentType = comments.itemContentType { it.javaClass },
    ) { index ->
        comments[index]?.let { comment ->
            val borderColor = KuringTheme.colors.gray600
            Comment(
                comment = comment,
                isReplyComment = (replyCommentId == comment.comment.id),
                onReplyIconClick = {
                    setReplyCommentId(if (replyCommentId == null) comment.comment.id else null)
                },
                onDeleteComment = {
                    onDeleteIconClick(comment.comment.id)
                    onDismissDropdown()
                },
                dropdownCommentId = dropdownCommentId,
                onDropdownShow = onDropdownShow,
                onDismissDropdown = onDismissDropdown,
                onReport = onReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp)
                    .drawBehind {
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 0.3.dp.toPx(),
                        )
                    },
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun LazyPagingCommentColumnPreview() {
    val fakeDataFlow = MutableStateFlow(PagingData.from(fakeComments(10)))
    val fakePagingData = fakeDataFlow.collectAsLazyPagingItems()
    KuringTheme {
        LazyPagingCommentColumn(
            comments = fakePagingData,
            replyCommentId = fakePagingData[0]!!.comment.id,
            setReplyCommentId = {},
            onReportComment = {},
            onDeleteIconClick = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}