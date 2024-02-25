package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ku_stacks.ku_ring.domain.Notice

@Stable
class SearchState(
    query: String,
    tab: SearchTabInfo,
) {
    var query by mutableStateOf(query)
    var tab by mutableStateOf(tab)
}
