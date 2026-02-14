package com.ku_stacks.ku_ring.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {

            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }
}
