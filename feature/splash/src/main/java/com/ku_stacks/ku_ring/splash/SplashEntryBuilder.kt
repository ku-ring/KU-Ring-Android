package com.ku_stacks.ku_ring.splash

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.compose.locals.LocalPreferences
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import com.ku_stacks.ku_ring.splash.compose.SplashScreen
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import com.ku_stacks.ku_ring.util.getAppVersionName
import com.ku_stacks.ku_ring.work.ReEngagementNotificationWork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class SplashEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<SplashKey> {
            val viewModel = hiltViewModel<SplashViewModel>()
            val screenState by viewModel.splashScreenState.collectAsStateWithLifecycle()
            val context = LocalContext.current
            val navigator = LocalNavigator.current
            val preferences = LocalPreferences.current

            // 초기화 로직 (원본 SplashActivity.onCreate에서 이전)
            LaunchedEffect(Unit) {
                // WorkManager re-engagement notification 예약
                enqueueReengagementNotificationWork(context)

                // 사용자 유효성 검증
                viewModel.getUserData()

                // 원격 학과 데이터 업데이트
                viewModel.updateDepartmentsFromRemote()

                // 최소 앱 버전 체크 (500ms delay 포함)
                delay(500)
                viewModel.checkUpdateRequired(context.getAppVersionName())
            }

            // 화면 분기 (원본 SplashActivity.collectScreenState → parseIntent)
            LaunchedEffect(screenState) {
                when (screenState) {
                    SplashScreenState.INITIAL,
                    SplashScreenState.LOADING,
                    SplashScreenState.UPDATE_REQUIRED -> {
                        // 대기
                    }
                    SplashScreenState.DISMISS_UPDATE,
                    SplashScreenState.UPDATE_NOT_REQUIRED -> {
                        delay(500)
                        val onboardingRequired = preferences.firstRunFlag && preferences.subscription.isEmpty()
                        if (onboardingRequired) {
                            createNotificationChannel(context)
                            navigator.navigateToOnboarding(context as android.app.Activity)
                        } else {
                            navigator.navigateToMain(context as android.app.Activity)
                        }
                    }
                }
            }

            SplashScreen(
                screenState = screenState,
                onUpdateApp = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.play_store_url))
                    )
                    context.startActivity(intent)
                },
                onDismissUpdateDialog = viewModel::dismissUpdateNotification,
                modifier = Modifier
                    .fillMaxSize()
                    .background(KuringTheme.colors.background),
            )
        }
    }

    private fun enqueueReengagementNotificationWork(context: Context) {
        val currentTime = System.currentTimeMillis()
        val afterOneWeek =
            DateUtil.getCalendar(dayToAdd = 7, hour = 12, minute = 0, second = 0) ?: return
        val delayInMillis = afterOneWeek.timeInMillis - currentTime

        val notificationWorkRequest = OneTimeWorkRequestBuilder<ReEngagementNotificationWork>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ReEngagementNotificationWork.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            notificationWorkRequest,
        )
    }

    private fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                KuringNotificationManager.CHANNEL_ID,
                KuringNotificationManager.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SplashEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideSplashEntryBuilder(): EntryBuilderProvider = SplashEntryBuilder()
}
