package com.ku_stacks.ku_ring.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.notification.compose.innerscreen.NotificationScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {
                NotificationScreen(
                    onNavigateUp = ::finish,
                    onNavigateToEditSubscription = ::navigateToEditSubscription,
                    onNotificationClick = ::determineNavigationTarget
                )
            }
        }
    }

    private fun navigateToEditSubscription() {
        navigator.navigateToEditSubscription(this)
    }

    private fun determineNavigationTarget(notification: Notification) {
        // TODO: 이동 화면 추가
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }
}
