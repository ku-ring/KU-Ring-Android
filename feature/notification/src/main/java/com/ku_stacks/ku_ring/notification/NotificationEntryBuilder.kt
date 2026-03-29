package com.ku_stacks.ku_ring.notification

import androidx.activity.compose.LocalActivity
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.domain.NotificationContent
import com.ku_stacks.ku_ring.navigation.keys.EditSubscriptionKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
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
            NotificationScreen(
                onNavigateUp = { navigator.goBack() },
                onNavigateToEditSubscription = { navigator.navigate(EditSubscriptionKey) },
                onNotificationClick = { notification ->
                    val content = notification.content
                    if (content is NotificationContent.Notice) {
                        navigator.navigate(
                            NoticeWebKey(
                                url = content.fullUrl,
                                articleId = content.articleId,
                                id = notification.id.toString(),
                                category = content.noticeCategory,
                                subject = content.subject,
                            )
                        )
                    }
                },
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
