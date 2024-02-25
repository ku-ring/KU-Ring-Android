package com.ku_stacks.ku_ring.main.search.compose.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.domain.Staff
import kotlinx.coroutines.flow.StateFlow

@Composable
fun StaffSearchScreen(
    staffSearchResult: StateFlow<List<Staff>>,
    modifier: Modifier = Modifier,
) {
    val staffList = staffSearchResult.collectAsState(initial = emptyList()).value

    LazyColumn(modifier) {
        items(staffList) {
            Text(text = it.name)
        }
    }
}
