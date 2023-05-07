package com.ku_stacks.ku_ring.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySplashBinding
import com.ku_stacks.ku_ring.ui.main.MainActivity
import com.ku_stacks.ku_ring.ui.onboarding.OnboardingActivity
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.FcmUtil
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.UrlGenerator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var fcmUtil: FcmUtil

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            delay(1000)

            when {
                launchedFromNoticeNotificationEvent(intent) -> {
                    handleNoticeNotification(intent)
                }
                launchedFromCustomNotificationEvent(intent) -> {
                    handleCustomNotification()
                }
                onboadingRequired() -> {
                    startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                }
                else -> {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
        }
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
            val baseUrl = map["baseUrl"] ?: return
            val url = UrlGenerator.generateNoticeUrl(
                articleId = articleId,
                category = category,
                baseUrl = baseUrl
            )

            fcmUtil.insertNotificationIntoDatabase(
                articleId = articleId,
                category = category,
                postedDate = postedDate,
                subject = subject,
                baseUrl = baseUrl,
                receivedDate = DateUtil.getCurrentTime()
            )

            MainActivity.start(
                activity = this@SplashActivity,
                url = url,
                articleId = articleId,
                category = category,
                postedDate = postedDate,
                subject = subject
            )
        }
    }

    private fun launchedFromCustomNotificationEvent(intent: Intent): Boolean {
        val data = intent.extras?.toMap()
            ?: return false
        return fcmUtil.isCustomNotification(data)
    }

    private fun handleCustomNotification() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun onboadingRequired(): Boolean {
        return pref.firstRunFlag && pref.subscription.isNullOrEmpty()
    }
}
