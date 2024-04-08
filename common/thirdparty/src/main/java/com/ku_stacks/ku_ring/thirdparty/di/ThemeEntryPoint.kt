package com.ku_stacks.ku_ring.thirdparty.di

import androidx.compose.runtime.staticCompositionLocalOf
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface KuringThemeEntryPoint {
    fun analytics(): EventAnalytics
    fun preference(): PreferenceUtil
    fun navigator(): KuringNavigator
}


val LocalNavigator = staticCompositionLocalOf<KuringNavigator> {
    error("No Navigator Provided")
}

val LocalPreferences = staticCompositionLocalOf {
    error("No Preferences Provided")
}

val LocalAnalytics = staticCompositionLocalOf {
    error("No Analytics Provided")
}
