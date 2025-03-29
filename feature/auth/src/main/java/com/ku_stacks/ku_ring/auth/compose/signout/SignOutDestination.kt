package com.ku_stacks.ku_ring.auth.compose.signout

import kotlinx.serialization.Serializable

// TODO: AuthDestination으로 이동할 예정
sealed interface SignOutDestination {
    @Serializable
    data object SignOut : SignOutDestination

    @Serializable
    data object SignOutGuide : SignOutDestination

    @Serializable
    data object SignOutComplete : SignOutDestination
}