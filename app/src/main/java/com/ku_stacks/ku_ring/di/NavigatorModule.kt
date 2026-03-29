package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.navigation.DeepLinkIntentFactory
import com.ku_stacks.ku_ring.navigator.DeepLinkIntentFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindDeepLinkIntentFactory(impl: DeepLinkIntentFactoryImpl): DeepLinkIntentFactory
}
