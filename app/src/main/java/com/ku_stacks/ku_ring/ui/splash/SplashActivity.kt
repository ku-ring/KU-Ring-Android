package com.ku_stacks.ku_ring.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySplashBinding
import com.ku_stacks.ku_ring.ui.main.MainActivity
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

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            delay(1000)

            // 저장한 구독목록이 있거나 firstRunFlag 가 false 면 바로 홈화면 진입
            if (!pref.firstRunFlag || !pref.subscription.isNullOrEmpty()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
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