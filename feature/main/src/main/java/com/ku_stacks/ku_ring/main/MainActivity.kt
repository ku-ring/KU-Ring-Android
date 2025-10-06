package com.ku_stacks.ku_ring.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.IntentCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

    @Inject
    lateinit var academicEventRepository: AcademicEventRepository

    lateinit var navController: NavHostController

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val notice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java
        )
        notice?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }

        intent.parseMainScreenRoute()?.let { route ->
            navController.navigate(route)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchAcademicEvent()

        val notice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java
        )
        notice?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }

        setContent {
            navController = rememberNavController()
            val startDestination = intent.parseMainScreenRoute() ?: MainScreenRoute.Notice
            KuringTheme {
                MainScreen(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KuringTheme.colors.background),
                    startDestination = startDestination,
                )
            }
        }
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        navigator.navigateToNoticeWeb(this, webViewNotice)
    }

    private fun fetchAcademicEvent() {
        lifecycleScope.launch {
            academicEventRepository.fetchAcademicEventsFromRemote()
        }
    }

    private fun Intent.parseMainScreenRoute(): MainScreenRoute? =
        getStringExtra(INTENT_KEY_ROUTE)?.let { route ->
            try {
                MainScreenRoute.of(route)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

    companion object {
        fun createIntent(context: Context, route: MainScreenRoute? = null) =
            Intent(context, MainActivity::class.java).apply {
                route?.let {
                    putExtra(INTENT_KEY_ROUTE, route.route)
                }
            }

        fun start(
            activity: Activity,
            url: String,
            articleId: String,
            id: Int,
            category: String,
            subject: String,
        ) {
            val intent = createIntent(activity).apply {
                putExtra(
                    WebViewNotice.EXTRA_KEY,
                    WebViewNotice(url, articleId, id, category, subject),
                )
            }
            activity.startActivity(intent)
        }

        fun start(activity: Activity) {
            val intent = createIntent(activity)
            activity.startActivity(intent)
        }

        private const val INTENT_KEY_ROUTE = "MAIN_SCREEN_ROUTE"
    }
}
