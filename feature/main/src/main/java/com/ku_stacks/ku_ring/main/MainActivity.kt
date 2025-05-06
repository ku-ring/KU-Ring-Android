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
import androidx.navigation.compose.rememberNavController
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java
        )
        notice?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }

        setContent {
            KuringTheme {
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KuringTheme.colors.background),
                )
            }
        }
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        navigator.navigateToNoticeWeb(this, webViewNotice)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)

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
    }
}
