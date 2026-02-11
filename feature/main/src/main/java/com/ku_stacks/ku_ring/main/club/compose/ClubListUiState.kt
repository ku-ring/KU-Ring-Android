package com.ku_stacks.ku_ring.main.club.compose

import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.ui.club.ClubSortOption

data class ClubListUiState(
    val selectedCategory: ClubCategory,
    val selectedDivisions: Set<ClubDivision>,
    val sortOption: ClubSortOption,
    val isDivisionBottomSheetVisible: Boolean,
) {
    companion object {
        fun empty() = ClubListUiState(
            selectedCategory = ClubCategory.ACADEMIC,
            selectedDivisions = emptySet(),
            sortOption = ClubSortOption.END_OF_RECRUITMENT,
            isDivisionBottomSheetVisible = false,
        )
    }
}
