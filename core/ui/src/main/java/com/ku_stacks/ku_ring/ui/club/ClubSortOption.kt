package com.ku_stacks.ku_ring.ui.club

enum class ClubSortOption(
    val text: String,
    val value: String,
) {
    END_OF_RECRUITMENT(
        text = "모집 마감일 순",
        value = "recruitEndDate"
    ),
    ALPHABETIC(
        text = "가나다 순",
        value = "name"
    ),
    ;
}
