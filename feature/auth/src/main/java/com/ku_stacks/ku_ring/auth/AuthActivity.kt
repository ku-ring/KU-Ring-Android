package com.ku_stacks.ku_ring.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.auth.compose.AuthScreen
import com.ku_stacks.ku_ring.feature.auth.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

            BackHandler {
                overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
            }
            AuthScreen(
                onNavigateUp = {
                    onBackPressedDispatcherOwner?.onBackPressedDispatcher.onBackPressed()
                }
            )
        }
    }

    companion object {
        fun start(context: Context) {
            with(context) {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
        }
    }
}