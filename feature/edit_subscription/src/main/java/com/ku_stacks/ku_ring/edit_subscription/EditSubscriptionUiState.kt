package com.ku_stacks.ku_ring.edit_subscription

import com.ku_stacks.ku_ring.edit_subscription.uimodel.DepartmentSubscriptionUiModel
import com.ku_stacks.ku_ring.edit_subscription.uimodel.NormalSubscriptionUiModel

data class EditSubscriptionUiState(
    val categories: List<NormalSubscriptionUiModel>,
    val departments: List<DepartmentSubscriptionUiModel>,
    val selectedTab: EditSubscriptionTab,
) {
    companion object {
        val initialValue = EditSubscriptionUiState(
            categories = emptyList(),
            departments = emptyList(),
            selectedTab = EditSubscriptionTab.NORMAL,
        )
    }
}