package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.navigator.KuringNavigatorImpl
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {
    @Binds
    abstract fun bindKuringNavigator(kuringNavigatorImpl: KuringNavigatorImpl): KuringNavigator
}