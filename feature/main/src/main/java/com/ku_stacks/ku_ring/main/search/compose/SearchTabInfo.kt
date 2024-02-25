package com.ku_stacks.ku_ring.main.search.compose

import androidx.annotation.StringRes
import com.ku_stacks.ku_ring.main.R

enum class SearchTabInfo(
    @StringRes val titleResId: Int,
) {
    Notice(R.string.search_type_notice),
    Staff(R.string.search_type_professor),
}
