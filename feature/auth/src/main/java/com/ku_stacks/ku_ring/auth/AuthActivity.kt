package com.ku_stacks.ku_ring.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.auth.compose.AuthDestination
import com.ku_stacks.ku_ring.auth.compose.AuthScreen
import com.ku_stacks.ku_ring.feature.auth.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val startDestination = if (intent.getStringExtra(INTENT_KEY) == INTENT_SIGN_OUT) {
                AuthDestination.SignOut
            } else {
                AuthDestination.SignIn
            }

            AuthScreen(
                onNavigateUp = ::finish,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.anim_slide_right_enter,
                R.anim.anim_slide_right_exit
            )
        } else {
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_slide_right_exit)
        }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                R.anim.anim_slide_left_enter,
                R.anim.anim_slide_left_exit
            )
        } else {
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }
    }

    companion object {
        private const val INTENT_KEY = "route"
        private const val INTENT_SIGN_OUT = "signout"
        private const val INTENT_AUTH = "auth"

        fun startAuth(context: Context) {
            with(context) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(INTENT_KEY, INTENT_AUTH)
                startActivity(intent)
            }
        }

        fun startSignOut(context: Context) {
            with(context) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(INTENT_KEY, INTENT_SIGN_OUT)
                startActivity(intent)
            }
        }
    }
}
