package com.ku_stacks.ku_ring.club.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubOnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {
                ClubOnboardingScreen(onClose = ::finish)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ClubOnboardingActivity::class.java))
        }
    }
}