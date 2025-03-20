package com.ku_stacks.ku_ring.auth.compose.signin

sealed class SignInSideEffect {
    data object NavigateToMain : SignInSideEffect()
}