package com.ku_stacks.ku_ring.edit_subscription

import androidx.annotation.StringRes

enum class EditSubscriptionTab(
    @param:StringRes val tabTitleId: Int,
    val analyticsName: String,
) {
    NORMAL(R.string.normal_subscription_tab_title, "일반카테고리"),
    DEPARTMENT(R.string.department_subscription_tab_title, "학과카테고리");
}
