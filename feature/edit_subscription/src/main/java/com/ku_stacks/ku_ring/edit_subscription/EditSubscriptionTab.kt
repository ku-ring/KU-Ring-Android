package com.ku_stacks.ku_ring.edit_subscription

import androidx.annotation.StringRes

enum class EditSubscriptionTab(
    @StringRes val tabTitleId: Int,
) {
    NORMAL(R.string.normal_subscription_tab_title),
    DEPARTMENT(R.string.department_subscription_tab_title);
}