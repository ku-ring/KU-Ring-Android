package com.ku_stacks.ku_ring.main.search.compose.inner_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.domain.Staff

@Composable
fun StaffSearchScreen(
    staffList: List<Staff>,
    modifier: Modifier = Modifier,
) {

    LazyColumn(modifier) {
        items(staffList) {
            Text(text = it.name)
        }
    }
}
