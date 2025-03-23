package com.ku_stacks.ku_ring.notice_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.notice_detail.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun CommentsBottomSheet(
    comments: LazyPagingItems<NoticeComment>?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.comment_bottom_sheet_title),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.64.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(600),
                color = KuringTheme.colors.textTitle,
            ),
            modifier = Modifier.padding(vertical = 10.dp),
        )
        LazyPagingCommentColumn(
            comments = comments,
            onReplyIconClick = { /* TODO: implement */ },
            onDeleteIconClick = { /* TODO: implement */ },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .weight(1f),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun CommentsBottomSheetPreview() {
    val fakeDataFlow = MutableStateFlow(PagingData.from(fakeComments))
    val fakePagingData = fakeDataFlow.collectAsLazyPagingItems()

    KuringTheme {
        CommentsBottomSheet(
            comments = fakePagingData,
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}