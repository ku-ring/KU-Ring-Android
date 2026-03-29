package com.ku_stacks.ku_ring.notification

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
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
            val context = LocalContext.current
            NotificationScreen(
                onNavigateUp = { (context as? Activity)?.finish() },
                onNavigateToEditSubscription = { navigator.navigateToEditSubscription(context as Activity) },
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
