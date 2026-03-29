package com.ku_stacks.ku_ring.club.detail

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
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
            val navigator = LocalNavigator.current
            val context = LocalContext.current
            val viewModel = hiltViewModel<ClubDetailViewModel>()
            viewModel.initClubId(key.clubId)
            ClubDetailScreen(
                viewModel = viewModel,
                onBack = { navigator.goBack() },
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
