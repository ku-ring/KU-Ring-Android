package com.ku_stacks.ku_ring.club.subscription

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.ClubSubscriptionKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class ClubSubscriptionEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<ClubSubscriptionKey> {
            val context = LocalContext.current
            ClubSubscriptionScreen(
                onNavigateUp = { (context as? Activity)?.finish() },
                onNavigateToClubDetail = { },
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ClubSubscriptionEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideClubSubscriptionEntryBuilder(): EntryBuilderProvider = ClubSubscriptionEntryBuilder()
}
