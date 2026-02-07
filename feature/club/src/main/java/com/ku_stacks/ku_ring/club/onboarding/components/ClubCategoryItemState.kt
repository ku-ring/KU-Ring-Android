package com.ku_stacks.ku_ring.club.onboarding.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ClubCategoryItemState(
    @StringRes val categoryName: Int,
    @StringRes val description: Int,
    val categoryId: String,
    @DrawableRes val iconId: Int,
)
