package com.ku_stacks.ku_ring.edit_subscription

data class SubscriptionUiModel(
    val content: String,
    val isNotificationEnabled: Boolean,
) {
    fun toggle() = SubscriptionUiModel(content, !isNotificationEnabled)
}