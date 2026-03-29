package com.ku_stacks.ku_ring.notice_detail

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class NoticeWebEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<NoticeWebKey> { key ->
            val activity = LocalActivity.current
            val webViewNotice = WebViewNotice(
                url = key.url,
                articleId = key.articleId,
                id = key.id.toIntOrNull() ?: 0,
                category = key.category,
                subject = key.subject,
            )
            val navController = rememberNavController()
            NoticeDetailScreen(
                navController = navController,
                webViewNotice = webViewNotice,
                onClose = { activity?.finish() },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NoticeWebEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideNoticeWebEntryBuilder(): EntryBuilderProvider = NoticeWebEntryBuilder()
}
