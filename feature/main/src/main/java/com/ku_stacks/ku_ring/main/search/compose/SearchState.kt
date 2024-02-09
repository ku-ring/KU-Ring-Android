package com.ku_stacks.ku_ring.main.search.compose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

@Stable
class SearchState(
    query: TextFieldValue,
    currentTab: String,
) {
    var query by mutableStateOf(query)
    var currentTab by mutableStateOf(currentTab)
}
