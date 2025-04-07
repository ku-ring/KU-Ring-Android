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

    @Serializable
    data object SignOut : AuthDestination

    @Serializable
    data object SignOutGuide : AuthDestination

    @Serializable
    data object SignOutComplete : AuthDestination

    companion object {
        /*
         * 화면 접근 순서를 나타내는 번호를 반환하는 함수
         * SignIn(0)을 거쳐야 SignUp, ResetPassword 흐름으로 진입 가능
         * 이후 각 플로우는 화면 이름과 순서대로 접근 가능
         */
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
                route.hasRoute(SignOut::class) -> 0
                route.hasRoute(SignOutGuide::class) -> 1
                route.hasRoute(SignOutComplete::class) -> 2
                else -> 6
            }
    }
}
