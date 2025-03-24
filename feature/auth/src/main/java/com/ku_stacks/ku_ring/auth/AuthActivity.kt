package com.ku_stacks.ku_ring.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.auth.compose.AuthScreen
import com.ku_stacks.ku_ring.feature.auth.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthScreen(
                onNavigateUp = this::finish,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            )
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
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