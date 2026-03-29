package com.ku_stacks.ku_ring

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.IntentCompat
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ku_stacks.ku_ring.compose.locals.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.NavigationController
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import com.ku_stacks.ku_ring.navigator.KuringNavigatorImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HostActivity : ComponentActivity() {

    @Inject
    lateinit var entryBuilders: @JvmSuppressWildcards Set<EntryBuilderProvider>

    @Inject
    lateinit var navigatorImpl: KuringNavigatorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // FCM 알림으로 앱이 cold start된 경우 Intent에서 WebViewNotice 추출
        handleIntent(intent)

        setContent {
            KuringCompositionLocalProvider {
                KuringTheme {
                    val backStack = rememberNavBackStack(SplashKey)

                    DisposableEffect(backStack) {
                        navigatorImpl.navigationController = object : NavigationController {
                            override fun navigate(key: NavKey) {
                                backStack.add(key)
                            }

                            override fun popBackStack() {
                                backStack.removeLastOrNull()
                            }
                        }
                        onDispose {
                            navigatorImpl.navigationController = null
                        }
                    }

                    NavDisplay(
                        backStack = backStack,
                        onBack = { if (backStack.removeLastOrNull() == null) finish() },
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
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        // WebViewNotice가 Intent extras에 있으면 해당 화면으로 이동
        val webViewNotice = IntentCompat.getSerializableExtra(
            intent,
            WebViewNotice.EXTRA_KEY,
            WebViewNotice::class.java,
        )
        if (webViewNotice != null) {
            navigatorImpl.navigationController?.navigate(
                NoticeWebKey(
                    url = webViewNotice.url,
                    articleId = webViewNotice.articleId,
                    id = webViewNotice.id.toString(),
                    category = webViewNotice.category,
                    subject = webViewNotice.subject,
                )
            )
            return
        }

        // MainScreenRoute가 Intent extras에 있으면 해당 탭으로 이동
        val route = intent.getStringExtra(INTENT_KEY_ROUTE)
        if (route != null) {
            navigatorImpl.navigationController?.navigate(MainHubKey(startTab = route))
            return
        }
    }

    companion object {
        private const val INTENT_KEY_ROUTE = "MAIN_SCREEN_ROUTE"
    }
}
