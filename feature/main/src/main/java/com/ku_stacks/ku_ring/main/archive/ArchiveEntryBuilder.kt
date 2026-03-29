package com.ku_stacks.ku_ring.main.archive

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.main.archive.compose.ArchiveScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.ArchiveKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class ArchiveEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<ArchiveKey> {
            ArchiveScreen()
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ArchiveEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideArchiveEntryBuilder(): EntryBuilderProvider = ArchiveEntryBuilder()
}
