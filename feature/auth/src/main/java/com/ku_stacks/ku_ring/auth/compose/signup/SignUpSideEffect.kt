package com.ku_stacks.ku_ring.auth.compose.signup

sealed interface SignUpSideEffect {
    data object NavigateToSetPassword : SignUpSideEffect
    data object NavigateToComplete : SignUpSideEffect
}