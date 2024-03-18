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
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    @Inject
    lateinit var analytics: EventAnalytics

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KuringTheme {
                OnboardingScreen(
                    onNavigateToMain = ::onNavigateToMain,
                    modifier = Modifier
                        .background(KuringTheme.colors.background)
                        .fillMaxSize(),
                )
            }
        }
    }

    private fun onNavigateToMain() {
        navigator.navigateToMain(this)
        analytics.click(
            screenName = "start first Subscription Notification",
            screenClass = "OnboardingActivity",
        )
        pref.firstRunFlag = false
        finish()
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, OnboardingActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
