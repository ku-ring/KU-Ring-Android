package com.ku_stacks.ku_ring.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.domain.NotificationCategory
import com.ku_stacks.ku_ring.domain.NotificationContent
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
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
        when (notification.category) {
            NotificationCategory.ACADEMIC_EVENT -> {
                navigateToAcademicEvent()
            }

            NotificationCategory.CLUB -> {
                navigateToClubDetail(notification.content as NotificationContent.Club)
            }

            NotificationCategory.NOTICE -> {
                navigateToNotice(notification)
            }

            else -> {} // 이외의 알림들은 처리하지 않음
        }
    }

    private fun navigateToAcademicEvent() {
        val intent = navigator.createMainIntent(this, MainScreenRoute.Calendar)
        startActivity(intent)
    }

    private fun navigateToClubDetail(content: NotificationContent.Club) {
        val clubId = content.clubId
        navigator.navigateToClubDetail(this, clubId)
    }

    private fun navigateToNotice(notification: Notification) {
        val webViewNotice = notification.content as NotificationContent.Notice
        val intent = navigator.createNoticeWebIntent(
            context = this,
            id = notification.id,
            articleId = webViewNotice.articleId,
            url = webViewNotice.fullUrl,
            category = webViewNotice.noticeCategory,
            subject = webViewNotice.subject,
        )
        startActivity(intent)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NotificationActivity::class.java))
        }
    }
}
