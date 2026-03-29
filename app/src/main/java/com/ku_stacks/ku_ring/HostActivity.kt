package com.ku_stacks.ku_ring

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.IntentCompat
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ku_stacks.ku_ring.compose.locals.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.DeepLinkIntentFactory
import com.ku_stacks.ku_ring.navigation.Navigator
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HostActivity : ComponentActivity() {

    @Inject
    lateinit var entryBuilders: @JvmSuppressWildcards Set<EntryBuilderProvider>

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // FCM 알림으로 앱이 cold start된 경우, Splash 완료 후 딥링크 처리
        handleIntent(intent, deferred = true)

        setContent {
            KuringCompositionLocalProvider {
                KuringTheme {
                    NavDisplay(
                        backStack = navigator.backStack,
                        onBack = {
                            if (navigator.backStack.size <= 1) {
                                finish()
                            } else {
                                navigator.goBack()
                            }
                        },
                        entryProvider = entryProvider {
                            entryBuilders.forEach { builder ->
                                with(builder) { provide() }
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent, deferred = false)
    }

    private fun handleIntent(intent: Intent, deferred: Boolean) {
        val key = extractNavKey(intent) ?: return
        if (deferred) {
            navigator.setPendingDeepLink(key)
        } else {
            navigator.navigate(key)
        }
    }

    private fun extractNavKey(intent: Intent): NavKey? {
        // WebViewNotice가 Intent extras에 있으면 해당 화면으로 이동
        val webViewNotice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java,
        )
        if (webViewNotice != null) {
            return NoticeWebKey(
                url = webViewNotice.url,
                articleId = webViewNotice.articleId,
                id = webViewNotice.id.toString(),
                category = webViewNotice.category,
                subject = webViewNotice.subject,
            )
        }

        // MainScreenRoute가 Intent extras에 있으면 해당 탭으로 이동
        val route = intent.getStringExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE)
        if (route != null) {
            return MainHubKey(startTab = route)
        }

        return null
    }
}
