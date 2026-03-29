package com.ku_stacks.ku_ring.main.club.compose

import androidx.annotation.StringRes

sealed class ClubListSideEffect {
    data class ShowToast(@StringRes val messageId: Int) : ClubListSideEffect()
}
