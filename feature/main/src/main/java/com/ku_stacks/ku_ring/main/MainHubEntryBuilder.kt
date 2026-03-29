package com.ku_stacks.ku_ring.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalPreferences
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class MainHubEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<MainHubKey> { key ->
            val navController = rememberNavController()
            val context = LocalContext.current
            val preferences = LocalPreferences.current
            val startDestination = try {
                MainScreenRoute.of(key.startTab)
            } catch (e: IllegalArgumentException) {
                MainScreenRoute.Notice
            }

            NotificationPermissionHandler(
                onPermissionGranted = { preferences.resetNotificationPermissionDialogCount() },
                canShowSettingsDialog = { preferences.canShowNotificationPermissionDialog() },
                onSettingsDialogShown = { preferences.notificationPermissionDialogCount++ },
                openAppNotificationSettings = {
                    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                    } else {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    }
                    context.startActivity(intent)
                },
            )

            MainScreen(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .background(KuringTheme.colors.background),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MainHubEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideMainHubEntryBuilder(): EntryBuilderProvider = MainHubEntryBuilder()
}
