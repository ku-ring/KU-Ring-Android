package com.ku_stacks.ku_ring.auth

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.auth.compose.AuthDestination
import com.ku_stacks.ku_ring.auth.compose.AuthScreen
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.AuthFlowKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class AuthFlowEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<AuthFlowKey> { key ->
            val navigator = LocalNavigator.current
            val startDestination = if (key.entryPoint == "SIGN_OUT") {
                AuthDestination.SignOut
            } else {
                AuthDestination.SignIn
            }
            AuthScreen(
                onNavigateUp = { navigator.goBack() },
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthFlowEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideAuthFlowEntryBuilder(): EntryBuilderProvider = AuthFlowEntryBuilder()
}
