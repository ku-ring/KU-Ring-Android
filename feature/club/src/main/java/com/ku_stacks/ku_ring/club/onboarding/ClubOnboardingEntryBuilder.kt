package com.ku_stacks.ku_ring.club.onboarding

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
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
            val context = LocalContext.current
            ClubOnboardingScreen(
                onClose = { (context as? Activity)?.finish() },
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
