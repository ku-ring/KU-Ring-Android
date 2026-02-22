package com.ku_stacks.ku_ring.club.subscription

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubSubscriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {
                ClubSubscriptionScreen(
                    onNavigateUp = ::finish,
                    onNavigateToClubDetail = { clubId ->
                        // TODO: 동아리 상세 화면으로 이동하는 로직 구현
                    },
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ClubSubscriptionActivity::class.java))
        }
    }
}
