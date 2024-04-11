package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.SfProDisplay
import com.ku_stacks.ku_ring.domain.Notice

@Composable
fun LazyPagingNoticeItemColumn(
    notices: LazyPagingItems<Notice>?,
    onNoticeClick: (Notice) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        when (notices?.loadState?.refresh) {
            LoadState.Loading -> {
                item {
                    PagingLoadingIndicator(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth()
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    LoadingErrorText(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth()
                    )
                }
            }

            else -> {
                notices?.let { notices ->
                    items(
                        count = notices.itemCount,
                        key = notices.itemKey(),
                        contentType = notices.itemContentType { it.javaClass },
                    ) { index ->
                        NoticeItem(
                            notice = notices[index] ?: return@items,
                            onClick = onNoticeClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PagingLoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.kus_green),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun LoadingErrorText(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.notice_refresh_error_message),
            fontFamily = SfProDisplay,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = colorResource(id = R.color.kus_label),
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
        )
    }
}