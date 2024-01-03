package com.ku_stacks.ku_ring.edit_subscription.uimodel

import androidx.annotation.DrawableRes
import com.ku_stacks.ku_ring.edit_subscription.R

data class NormalSubscriptionUiModel(
    val categoryName: String,
    @DrawableRes val categoryIconId: Int,
    val isSelected: Boolean,
) {
    fun toggle() = this.copy(isSelected = !isSelected)

    companion object {
        val initialValues = listOf(
            NormalSubscriptionUiModel("학사", R.drawable.bachelor, false),
            NormalSubscriptionUiModel("취창업", R.drawable.employment, false),
            NormalSubscriptionUiModel("국제", R.drawable.international, false),
            NormalSubscriptionUiModel("장학", R.drawable.scholarship, false),
            NormalSubscriptionUiModel("도서관", R.drawable.library, false),
            NormalSubscriptionUiModel("학생", R.drawable.student, false),
            NormalSubscriptionUiModel("산학", R.drawable.industry_university, false),
            NormalSubscriptionUiModel("일반", R.drawable.normal, false),
        )
    }
}
