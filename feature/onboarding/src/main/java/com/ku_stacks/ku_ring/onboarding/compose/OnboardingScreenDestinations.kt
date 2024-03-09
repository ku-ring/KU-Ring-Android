package com.ku_stacks.ku_ring.onboarding.compose

object OnboardingScreenDestinations {
    const val FEATURE_TABS = "feature_tabs"
    const val SET_DEPARTMENT = "set_department"
    const val CONFIRM_DEPARTMENT = "confirm_department"
    const val ONBOARDING_COMPLETE = "onboarding_complete"

    fun getOrder(destination: String?) = when (destination) {
        FEATURE_TABS -> 0
        SET_DEPARTMENT -> 1
        CONFIRM_DEPARTMENT -> 2
        ONBOARDING_COMPLETE -> 3
        else -> 4
    }
}