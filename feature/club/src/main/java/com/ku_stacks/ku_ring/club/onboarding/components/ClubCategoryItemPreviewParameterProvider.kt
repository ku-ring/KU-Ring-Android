package com.ku_stacks.ku_ring.club.onboarding.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.club.R

class ClubCategoryItemPreviewParameterProvider : PreviewParameterProvider<ClubCategoryItemState> {
    private val item = ClubCategoryItemState(
        categoryName = R.string.category_academic,
        description = R.string.description_academic,
        categoryId = "",
        iconId = R.drawable.ic_academic_v2,
    )

    override val values: Sequence<ClubCategoryItemState>
        get() = sequenceOf(item)
}