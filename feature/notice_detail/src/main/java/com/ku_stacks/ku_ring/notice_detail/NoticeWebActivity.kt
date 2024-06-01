package com.ku_stacks.ku_ring.notice_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Deprecated되지 않은 다른 함수가 API 33 이상에서만 사용할 수 있어서 부득이하게 deprecated 함수를 사용
        val webViewNotice = intent.getSerializableExtra(WebViewNotice.EXTRA_KEY) as? WebViewNotice
            ?: throw IllegalStateException("WebViewNotice should not be null.")

        setContent {
            KuringTheme {
                NoticeWebScreen(
                    webViewNotice = webViewNotice,
                    onNavigateBack = ::finish,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {

        fun start(activity: Activity, webViewNotice: WebViewNotice) {
            val (url, articleId, category, subject) = webViewNotice
            val intent = createIntent(activity, url, articleId, category, subject)
            activity.apply {
                startActivity(intent)
                overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
            }
        }

        fun createIntent(
            context: Context,
            url: String?,
            articleId: String?,
            category: String?,
            subject: String?,
        ): Intent {
            if (url == null || articleId == null || category == null) {
                throw IllegalArgumentException("intent parameters shouldn't be null: $url, $articleId, $category")
            }
            return Intent(context, NoticeWebActivity::class.java).apply {
                putExtra(
                    WebViewNotice.EXTRA_KEY, WebViewNotice(
                        url = url,
                        articleId = articleId,
                        category = category,
                        subject = subject.orEmpty(),
                    )
                )
            }
        }
    }
}
