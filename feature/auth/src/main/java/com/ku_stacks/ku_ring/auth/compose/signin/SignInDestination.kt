package com.ku_stacks.ku_ring.auth.compose.signin

import kotlinx.serialization.Serializable

sealed interface SignInDestination {
    @Serializable
    data object SignIn: SignInDestination
}