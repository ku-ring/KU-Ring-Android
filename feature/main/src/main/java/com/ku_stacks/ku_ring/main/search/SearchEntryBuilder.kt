package com.ku_stacks.ku_ring.main.search

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.mapper.toWebViewNotice
import com.ku_stacks.ku_ring.main.search.compose.SearchScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.SearchKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class SearchEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<SearchKey> {
            val viewModel = hiltViewModel<SearchViewModel>()
            val navigator = LocalNavigator.current
            val activity = LocalActivity.current
            SearchScreen(
                viewModel = viewModel,
                onNavigationClick = { activity?.finish() },
                onClickNotice = { activity?.let { act -> navigator.navigateToNoticeWeb(act, it.toWebViewNotice()) } },
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SearchEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideSearchEntryBuilder(): EntryBuilderProvider = SearchEntryBuilder()
}
