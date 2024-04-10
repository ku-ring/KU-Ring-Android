package com.ku_stacks.ku_ring.splash

/**
 *                   ┌ UpdateRequired ─ DismissUpdate
 * Initial ─ Loading ┤
 *                   └ UpdateNotRequired
 */
enum class SplashScreenState {
    INITIAL,
    LOADING,
    UPDATE_REQUIRED,
    DISMISS_UPDATE,
    UPDATE_NOT_REQUIRED,
}