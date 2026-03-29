package com.ku_stacks.ku_ring.club.onboarding

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.ClubOnboardingKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class ClubOnboardingEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<ClubOnboardingKey> {
            val navigator = LocalNavigator.current
            ClubOnboardingScreen(
                onClose = { navigator.goBack() },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ClubOnboardingEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideClubOnboardingEntryBuilder(): EntryBuilderProvider = ClubOnboardingEntryBuilder()
}
