package com.ku_stacks.ku_ring.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
            val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
            val startDestination = if (intent.getStringExtra(INTENT_KEY) == INTENT_SIGN_OUT) {
                AuthDestination.SignOut
            } else {
                AuthDestination.SignIn
            }

            BackHandler {
                overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
            }

            AuthScreen(
                onNavigateUp = {
                    onBackPressedDispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
                },
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            )
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
