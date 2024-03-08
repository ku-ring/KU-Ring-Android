package com.ku_stacks.ku_ring.onboarding.compose.inner_screen.feature_tab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ku_stacks.ku_ring.onboarding.R

enum class FeatureTabItem(
    @DrawableRes val imageId: Int,
    @StringRes val stringId: Int,
) {
    BELL(R.drawable.onboarding_image_bell, R.string.tab_bell),
    PHONE(R.drawable.onboarding_image_phone, R.string.tab_phone),
    SEARCH(R.drawable.onboarding_image_search, R.string.tab_search);
}
