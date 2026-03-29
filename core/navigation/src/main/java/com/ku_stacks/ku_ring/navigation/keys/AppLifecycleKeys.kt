package com.ku_stacks.ku_ring.navigation.keys

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data object SplashKey : NavKey
@Serializable data object OnboardingKey : NavKey
@Serializable data class AuthFlowKey(val entryPoint: AuthEntryPoint) : NavKey

@Serializable
enum class AuthEntryPoint {
    @SerialName("sign_in") SIGN_IN,
    @SerialName("sign_out") SIGN_OUT,
}
