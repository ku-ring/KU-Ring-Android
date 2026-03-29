package com.ku_stacks.ku_ring.kuringbot

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.kuringbot.compose.KuringBotScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.AuthEntryPoint
import com.ku_stacks.ku_ring.navigation.keys.AuthFlowKey
import com.ku_stacks.ku_ring.navigation.keys.KuringBotKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class KuringBotEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<KuringBotKey> {
            val navigator = LocalNavigator.current
            KuringBotScreen(
                onBackButtonClick = { navigator.goBack() },
                onMoveToLogin = { navigator.navigate(AuthFlowKey(AuthEntryPoint.SIGN_IN)) },
                modifier = Modifier
                    .imePadding()
                    .fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object KuringBotEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideKuringBotEntryBuilder(): EntryBuilderProvider = KuringBotEntryBuilder()
}
