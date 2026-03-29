package com.ku_stacks.ku_ring.notification

import androidx.activity.compose.LocalActivity
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.NotificationKey
import com.ku_stacks.ku_ring.notification.compose.innerscreen.NotificationScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class NotificationEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<NotificationKey> {
            val navigator = LocalNavigator.current
            val activity = LocalActivity.current
            NotificationScreen(
                onNavigateUp = { activity?.finish() },
                onNavigateToEditSubscription = { activity?.let { navigator.navigateToEditSubscription(it) } },
                onNotificationClick = {},
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NotificationEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideNotificationEntryBuilder(): EntryBuilderProvider = NotificationEntryBuilder()
}
