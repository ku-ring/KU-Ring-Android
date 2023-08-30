package com.ku_stacks.ku_ring.di

import com.ku_stacks.common.FeatureNavigator
import com.ku_stacks.ku_ring.util.FeatureNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class NavigatorModule {

    @Binds
    abstract fun bindsFeatureNavigator(
        impl: FeatureNavigatorImpl
    ) : FeatureNavigator
}
