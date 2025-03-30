package com.ku_stacks.ku_ring.auth.compose

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.serialization.Serializable


internal sealed interface AuthDestination {
    @Serializable
    data object SignIn : AuthDestination

    @Serializable
    data object SignUp : AuthDestination

    @Serializable
    data object SignUpTermsAndConditions : AuthDestination

    @Serializable
    data object SignUpEmailVerification : AuthDestination

    @Serializable
    data object SignUpSetPassword : AuthDestination

    @Serializable
    data object SignUpComplete : AuthDestination

    @Serializable
    data object ResetPassword : AuthDestination

    @Serializable
    data object ResetPasswordEmailVerification : AuthDestination

    @Serializable
    data object ResetPasswordSetPassword : AuthDestination

    companion object {
        fun getOrder(route: NavDestination): Int =
            when {
                route.hasRoute(SignIn::class) -> 0
                route.hasRoute(SignUp::class) -> 1
                route.hasRoute(SignUpTermsAndConditions::class) -> 2
                route.hasRoute(SignUpEmailVerification::class) -> 3
                route.hasRoute(SignUpSetPassword::class) -> 4
                route.hasRoute(SignUpComplete::class) -> 5
                route.hasRoute(ResetPassword::class) -> 1
                route.hasRoute(ResetPasswordEmailVerification::class) -> 2
                route.hasRoute(ResetPasswordSetPassword::class) -> 3
                else -> 6
            }
    }
}
