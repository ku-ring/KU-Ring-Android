package com.ku_stacks.ku_ring.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.IntentCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

    lateinit var navController: NavHostController

    @Inject
    lateinit var pref: PreferenceUtil

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val notice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java
        )
        notice?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }

        intent.parseMainScreenRoute()?.let { route ->
            navController.navigate(route)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java
        )
        notice?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }

        setContent {
            navController = rememberNavController()
            val startDestination = intent.parseMainScreenRoute() ?: MainScreenRoute.Notice
            KuringTheme {
                NotificationPermissionHandler(
                    onPermissionGranted = { pref.resetNotificationPermissionDialogCount() },
                    canShowSettingsDialog = { pref.canShowNotificationPermissionDialog() },
                    onSettingsDialogShown = { pref.notificationPermissionDialogCount++ },
                    openAppNotificationSettings = { launchAppNotificationSettings() }
                )

                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KuringTheme.colors.background),
                    startDestination = startDestination,
                )
            }
        }
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        navigator.navigateToNoticeWeb(this, webViewNotice)
    }

    private fun launchAppNotificationSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
        }
        startActivity(intent)
    }

    private fun Intent.parseMainScreenRoute(): MainScreenRoute? =
        getStringExtra(INTENT_KEY_ROUTE)?.let { route ->
            try {
                MainScreenRoute.of(route)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

    companion object {
        fun createIntent(context: Context, route: MainScreenRoute? = null) =
            Intent(context, MainActivity::class.java).apply {
                route?.let {
                    putExtra(INTENT_KEY_ROUTE, route.route)
                }
            }

        fun start(
            activity: Activity,
            url: String,
            articleId: String,
            id: Int,
            category: String,
            subject: String,
        ) {
            val intent = createIntent(activity).apply {
                putExtra(
                    WebViewNotice.EXTRA_KEY,
                    WebViewNotice(url, articleId, id, category, subject),
                )
            }
            activity.startActivity(intent)
        }

        fun start(activity: Activity) {
            val intent = createIntent(activity)
            activity.startActivity(intent)
        }

        private const val INTENT_KEY_ROUTE = "MAIN_SCREEN_ROUTE"
    }
}
