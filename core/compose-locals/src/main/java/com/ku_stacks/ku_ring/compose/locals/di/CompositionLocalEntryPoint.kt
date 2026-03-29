package com.ku_stacks.ku_ring.compose.locals.di

import androidx.compose.runtime.staticCompositionLocalOf
import com.ku_stacks.ku_ring.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.navigation.Navigator
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface KuringCompositionLocalEntryPoint {
    fun analytics(): EventAnalytics
    fun preference(): PreferenceUtil
    fun navigator(): Navigator
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator Provided")
}

val LocalPreferences = staticCompositionLocalOf<PreferenceUtil> {
    error("No Preferences Provided")
}

val LocalAnalytics = staticCompositionLocalOf<EventAnalytics> {
    error("No Analytics Provided")
}
