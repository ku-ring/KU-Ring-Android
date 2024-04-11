package com.ku_stacks.ku_ring.splash

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.splash.compose.SplashScreen
import com.ku_stacks.ku_ring.thirdparty.firebase.FcmUtil
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.ui_util.getAppVersionName
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import com.ku_stacks.ku_ring.work.ReEngagementNotificationWork
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var fcmUtil: FcmUtil

    @Inject
    lateinit var navigator: KuringNavigator

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KuringTheme {
                val screenState by viewModel.splashScreenState.collectAsStateWithLifecycle()
                SplashScreen(
                    screenState = screenState,
                    onUpdateApp = ::navigateToKuringPlayStore,
                    onDismissUpdateDialog = viewModel::dismissUpdateNotification,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KuringTheme.colors.background),
                )
            }
        }

        enqueueReengagementNotificationWork()
        loadMinimumAppVersion()
        collectScreenState()
    }

    private fun enqueueReengagementNotificationWork() {
        val currentTime = System.currentTimeMillis()
        val afterOneWeek =
            DateUtil.getCalendar(dayToAdd = 7, hour = 12, minute = 0, second = 0) ?: return
        val delayInMillis = afterOneWeek.timeInMillis - currentTime

        val notificationWorkRequest = OneTimeWorkRequestBuilder<ReEngagementNotificationWork>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            ReEngagementNotificationWork.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            notificationWorkRequest,
        )
    }

    private fun loadMinimumAppVersion() {
        lifecycleScope.launch {
            delay(500)
            viewModel.checkUpdateRequired(this@SplashActivity.getAppVersionName())
        }
    }

    private fun collectScreenState() {
        lifecycleScope.launch(Dispatchers.Default) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.splashScreenState.collectLatest { screenState ->
                    when (screenState) {
                        SplashScreenState.INITIAL, SplashScreenState.LOADING, SplashScreenState.UPDATE_REQUIRED -> {}
                        SplashScreenState.DISMISS_UPDATE, SplashScreenState.UPDATE_NOT_REQUIRED -> {
                            delay(500)
                            parseIntent()
                        }
                    }
                }
            }
        }
    }

    private fun parseIntent() {
        when {
            launchedFromNoticeNotificationEvent(intent) -> {
                handleNoticeNotification(intent)
            }

            launchedFromCustomNotificationEvent(intent) -> {
                handleCustomNotification()
            }

            onboardingRequired() -> {
                createNotificationChannel()
                navigator.navigateToOnboarding(this@SplashActivity)
            }

            else -> {
                navigator.navigateToMain(this@SplashActivity)
            }
        }
        finish()
    }

    private fun navigateToKuringPlayStore() {
        val playStoreIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.play_store_url)))
        startActivity(playStoreIntent)
    }

    private fun launchedFromNoticeNotificationEvent(intent: Intent): Boolean {
        val data = intent.extras?.toMap()
            ?: return false
        return fcmUtil.isNoticeNotification(data)
    }

    private fun Bundle.toMap(): Map<String, String?> {
        val map: MutableMap<String, String?> = HashMap()

        for (key in this.keySet()) {
            map[key] = this.getString(key)
        }
        return map
    }

    private fun handleNoticeNotification(intent: Intent) {
        intent.extras?.toMap()?.let { map ->
            val articleId = map["articleId"] ?: return
            val category = map["category"] ?: return
            val postedDate = map["postedDate"] ?: return
            val subject = map["subject"] ?: return
            val fullUrl = map["baseUrl"] ?: return

            fcmUtil.insertNotificationIntoDatabase(
                articleId = articleId,
                category = category,
                postedDate = postedDate,
                subject = subject,
                fullUrl = fullUrl,
                receivedDate = DateUtil.getCurrentTime()
            )

            navigator.navigateToMain(
                activity = this@SplashActivity,
                url = fullUrl,
                articleId = articleId,
                category = category,
                subject = subject,
            )
        }
    }

    private fun launchedFromCustomNotificationEvent(intent: Intent): Boolean {
        val data = intent.extras?.toMap()
            ?: return false
        return fcmUtil.isCustomNotification(data)
    }

    private fun handleCustomNotification() {
        navigator.navigateToMain(this)
    }

    private fun onboardingRequired(): Boolean {
        return pref.firstRunFlag && pref.subscription.isEmpty()
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                KuringNotificationManager.CHANNEL_ID,
                KuringNotificationManager.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun finish() {
        overridePendingTransition(0, 0)
        super.finish()
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, SplashActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
