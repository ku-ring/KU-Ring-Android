package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.NoticeItem
import com.ku_stacks.ku_ring.domain.Notice
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun NoticeSearchScreen(
    noticeSearchResult: SharedFlow<List<Notice>>,
    modifier: Modifier = Modifier,
) {
    val noticeList = noticeSearchResult.collectAsState(initial = emptyList()).value

    LazyColumn(modifier) {
        items(noticeList) {
            NoticeItem(
                notice = it,
                onClick = {}
            )
            Divider(
                color = Color.LightGray,
                thickness = 0.5.dp
            )
        }
    }

}
