package com.ku_stacks.ku_ring.main.search.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.NoticeItem
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.search.compose.SearchState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NoticeSearchScreen(
    searchState: SearchState,
    noticeSearchResult: StateFlow<List<Notice>>,
    modifier: Modifier = Modifier,
) {
    val noticeList = noticeSearchResult.collectAsState(initial = emptyList()).value

    if (searchState.isLoading) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.kus_green),
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    } else if(noticeList.isEmpty()) {
        EmptyResultScreen()
    } else {
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
}
