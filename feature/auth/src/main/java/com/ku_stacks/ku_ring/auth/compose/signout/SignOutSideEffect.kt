package com.ku_stacks.ku_ring.auth.compose.signout

sealed interface SignOutSideEffect {
    data object NavigateToSignOutComplete : SignOutSideEffect
}