package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Multibinds
    abstract fun bindEntryBuilderProviders(): Set<EntryBuilderProvider>
}
