package com.ku_stacks.ku_ring.edit_subscription

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.edit_subscription.compose.EditSubscriptionScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.EditSubscriptionKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class EditSubscriptionEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<EditSubscriptionKey> {
            val navigator = LocalNavigator.current
            val context = LocalContext.current
            EditSubscriptionScreen(
                onNavigateToBack = { (context as? Activity)?.finish() },
                onFinish = { (context as? Activity)?.finish() },
                onAddDepartmentButtonClick = {
                    navigator.navigateToEditSubscribedDepartment(context as Activity)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object EditSubscriptionEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideEditSubscriptionEntryBuilder(): EntryBuilderProvider = EditSubscriptionEntryBuilder()
}
