package com.ku_stacks.ku_ring.club.detail

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.ClubDetailKey
import com.ku_stacks.ku_ring.util.navigateToExternalBrowser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class ClubDetailEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<ClubDetailKey> { key ->
            val context = LocalContext.current
            ClubDetailScreen(
                onBack = { (context as? Activity)?.finish() },
                onMoveToRecruitmentLink = context::navigateToExternalBrowser,
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .safeDrawingPadding()
                    .fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ClubDetailEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideClubDetailEntryBuilder(): EntryBuilderProvider = ClubDetailEntryBuilder()
}
