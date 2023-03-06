package com.ku_stacks.ku_ring.ui.on_boarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui.edit_subscription.EditSubscriptionActivity
import com.ku_stacks.ku_ring.ui.main.MainActivity
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    @Inject
    lateinit var analytics: EventAnalytics

    @Inject
    lateinit var pref: PreferenceUtil

    private val getOnBoardingFinishResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val subscribeNoticeButton = findViewById<Button>(R.id.on_boarding_subscribe_noti_btn)

        subscribeNoticeButton.setOnClickListener {
            val intent = Intent(this, EditSubscriptionActivity::class.java).apply {
                putExtra(EditSubscriptionActivity.FIRST_RUN_FLAG, true)
            }
            getOnBoardingFinishResult.launch(intent)

            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
            analytics.click("start first Subscription Notification", "OnBoardingActivity")
        }
    }
}