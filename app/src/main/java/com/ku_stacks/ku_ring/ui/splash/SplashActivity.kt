package com.ku_stacks.ku_ring.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.home.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }
}