package com.ku_stacks.ku_ring.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.ui_util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

    private var backPressedTime = 0L

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        (intent.getSerializableExtra(WebViewNotice.EXTRA_KEY) as? WebViewNotice)?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KuringTheme {
                var currentRoute: MainScreenRoute by remember { mutableStateOf(MainScreenRoute.Notice) }
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    currentRoute = currentRoute,
                    onNavigateToRoute = {
                        if (currentRoute != it) {
                            currentRoute = it
                            navController.navigate(it)
                        }
                    },
                    modifier = Modifier.fillMaxSize().background(KuringTheme.colors.background),
                )
            }
        }

        (intent?.getSerializableExtra(WebViewNotice.EXTRA_KEY) as? WebViewNotice)?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        navigator.navigateToNoticeWeb(this, webViewNotice)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
        } else {
            showToast(getString(R.string.home_finish_if_back_again))
            backPressedTime = System.currentTimeMillis()
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)

        fun start(
            activity: Activity,
            url: String,
            articleId: String,
            category: String,
            subject: String,
        ) {
            val intent = createIntent(activity).apply {
                putExtra(
                    WebViewNotice.EXTRA_KEY,
                    WebViewNotice(url, articleId, category, subject),
                )
            }
            activity.startActivity(intent)
        }

        fun start(activity: Activity) {
            val intent = createIntent(activity)
            activity.startActivity(intent)
        }
    }
}
