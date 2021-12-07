package com.ku_stacks.ku_ring.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.home.HomeActivity
import com.ku_stacks.ku_ring.ui.on_boarding.OnBoardingActivity
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var pref: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000)

            //저장한 구독목록이 있거나 firstRunFlag 가 false 면 바로 홈화면
            if (!pref.firstRunFlag || !pref.subscription.isNullOrEmpty()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }
    }
}