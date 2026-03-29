package com.ku_stacks.ku_ring.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent().setClassName(this, "com.ku_stacks.ku_ring.HostActivity")
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }
}
