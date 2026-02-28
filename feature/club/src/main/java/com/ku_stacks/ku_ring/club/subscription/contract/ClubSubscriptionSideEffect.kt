package com.ku_stacks.ku_ring.club.subscription.contract

import androidx.annotation.StringRes

sealed class ClubSubscriptionSideEffect {
    data class ShowToast(@StringRes val messageId: Int) : ClubSubscriptionSideEffect()
}
