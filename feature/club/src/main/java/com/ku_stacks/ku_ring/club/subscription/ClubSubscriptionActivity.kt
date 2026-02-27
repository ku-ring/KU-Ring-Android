package com.ku_stacks.ku_ring.club.subscription

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClubSubscriptionActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {
                ClubSubscriptionScreen(
                    onNavigateUp = ::finish,
                    onNavigateToClubDetail = { clubId ->
                        navigator.navigateToClubDetail(this, clubId)
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
