package com.ku_stacks.ku_ring.notion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.NotionViewKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class NotionViewEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<NotionViewKey> { key ->
            NotionScreen(
                url = key.url,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NotionViewEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideNotionViewEntryBuilder(): EntryBuilderProvider = NotionViewEntryBuilder()
}
