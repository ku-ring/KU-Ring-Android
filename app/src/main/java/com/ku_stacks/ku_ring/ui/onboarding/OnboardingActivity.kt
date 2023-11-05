package com.ku_stacks.ku_ring.ui.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
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

    private val getOnboardingFinishResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            navigator.navigateToMain(this)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val subscribeNoticeButton = findViewById<Button>(R.id.on_boarding_subscribe_noti_btn)

        subscribeNoticeButton.setOnClickListener {
            val intent = navigator.createEditSubscriptionIntent(this, isFirstRun = true)
            getOnboardingFinishResult.launch(intent)

            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
            analytics.click("start first Subscription Notification", "OnboardingActivity")
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, OnboardingActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
