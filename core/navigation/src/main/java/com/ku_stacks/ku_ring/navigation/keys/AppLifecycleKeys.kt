package com.ku_stacks.ku_ring.navigation.keys

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object SplashKey : NavKey
@Serializable data object OnboardingKey : NavKey
@Serializable data class AuthFlowKey(val entryPoint: String) : NavKey
