package com.ku_stacks.ku_ring.auth.compose.reset_password

sealed interface ResetPasswordSideEffect {
    data object NavigateToResetPassword : ResetPasswordSideEffect
    data object NavigateToSignIn : ResetPasswordSideEffect
}