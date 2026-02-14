package com.ku_stacks.ku_ring.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.IntentCompat
import androidx.navigation.NavHostController
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.checkHasNotificationPermission
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
                var isOpenSettingsDialogVisible by rememberSaveable { mutableStateOf(false) }
                var isAppSettingsOpened by rememberSaveable { mutableStateOf(false) }

                // 앱 설정 이동 후에 다시 확인
                LifecycleResumeEffect(Unit) {
                    if (baseContext.checkHasNotificationPermission()
                        && isAppSettingsOpened
                    ) {
                        pref.notificationPermissionDialogCount = 0
                        isAppSettingsOpened = false
                    }
                    onPauseOrDispose {}
                }

                RequestNotificationPermission(
                    onGranted = {
                        pref.resetNotificationPermissionDialogCount()
                    },
                    onDenied = {
                        if (pref.canShowNotificationPermissionDialog()) {
                            isOpenSettingsDialogVisible = true
                        }
                    },
                )

                if (isOpenSettingsDialogVisible) {
                    KuringAlertDialog(
                        text = stringResource(R.string.notification_permission_dialog_text),
                        confirmText = stringResource(R.string.notification_permission_dialog_confirm),
                        onConfirm = {
                            isOpenSettingsDialogVisible = false
                            pref.notificationPermissionDialogCount++
                            launchAppSettings()
                        },
                        onCancel = {
                            isAppSettingsOpened = true
                            isOpenSettingsDialogVisible = false
                            pref.notificationPermissionDialogCount++
                        },
                    )
                }

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

    @Composable
    private fun RequestNotificationPermission(
        onGranted: () -> Unit = {},
        onDenied: () -> Unit = {},
    ) {
        if (!baseContext.checkHasNotificationPermission()) {
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { isGranted ->
                if (isGranted) onGranted() else onDenied()
            }

            LaunchedEffect(Unit) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun launchAppSettings() {
        navigator.navigateToAppNotificationSettings(this)
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        navigator.navigateToNoticeWeb(this, webViewNotice)
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
