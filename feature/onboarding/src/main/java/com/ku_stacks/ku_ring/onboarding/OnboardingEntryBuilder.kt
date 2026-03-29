package com.ku_stacks.ku_ring.onboarding

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalAnalytics
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.compose.locals.LocalPreferences
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.OnboardingKey
import com.ku_stacks.ku_ring.onboarding.compose.OnboardingScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class OnboardingEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<OnboardingKey> {
            val navigator = LocalNavigator.current
            val analytics = LocalAnalytics.current
            val preferences = LocalPreferences.current
            val activity = LocalActivity.current
            OnboardingScreen(
                onNavigateToMain = {
                    activity?.let {
                        navigator.navigateToMain(it)
                        analytics.click(
                            screenName = "start first Subscription Notification",
                            screenClass = "OnboardingActivity",
                        )
                        preferences.firstRunFlag = false
                        it.finish()
                    }
                },
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object OnboardingEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideOnboardingEntryBuilder(): EntryBuilderProvider = OnboardingEntryBuilder()
}
