package com.ku_stacks.ku_ring.main.club.compose

import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.ui.club.ClubSortOption

sealed interface ClubListUiState {
    data object Loading : ClubListUiState
    data class Success(val clubSummaries: List<ClubSummary>) : ClubListUiState
    data class Error(val message: String?) : ClubListUiState
}

data class ClubListFilter(
    val selectedCategory: ClubCategory,
    val selectedDivisions: Set<ClubDivision>,
    val sortOption: ClubSortOption,
) {
    companion object {
        fun default() = ClubListFilter(
            selectedCategory = ClubCategory.ALL,
            selectedDivisions = setOf(),
            sortOption = ClubSortOption.END_OF_RECRUITMENT
        )
    }
}
