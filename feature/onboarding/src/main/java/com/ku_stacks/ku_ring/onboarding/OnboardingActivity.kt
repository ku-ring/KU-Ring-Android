package com.ku_stacks.ku_ring.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.onboarding.compose.OnboardingScreen
import com.ku_stacks.ku_ring.thirdparty.compose.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.thirdparty.di.LocalAnalytics
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import com.ku_stacks.ku_ring.thirdparty.di.LocalPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KuringCompositionLocalProvider {
                val navigator = LocalNavigator.current
                val analytics = LocalAnalytics.current
                val preferences = LocalPreferences.current
                KuringTheme {
                    OnboardingScreen(
                        onNavigateToMain = {
                            navigator.navigateToMain(this)
                            analytics.click(
                                screenName = "start first Subscription Notification",
                                screenClass = "OnboardingActivity",
                            )
                            preferences.firstRunFlag = false
                            finish()
                        },
                        modifier = Modifier
                            .background(KuringTheme.colors.background)
                            .fillMaxSize(),
                    )
                }
            }
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, OnboardingActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
