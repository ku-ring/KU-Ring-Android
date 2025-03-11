package com.ku_stacks.ku_ring.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.auth.compose.AuthScreen
import feature.auth.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthScreen(
                onNavigateUp = this::finish
            )
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        fun start(activity: Activity) {
            with(activity) {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }
}