package com.ku_stacks.ku_ring.notice_detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.domain.WebViewNotice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeWebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val redirectIntent = Intent().setClassName(this, "com.ku_stacks.ku_ring.HostActivity")
        intent.extras?.let { redirectIntent.putExtras(it) }
        startActivity(redirectIntent)
        finish()
    }

    companion object {
        fun start(activity: Activity, webViewNotice: WebViewNotice) {
            val intent = createIntent(activity, webViewNotice)
            activity.apply {
                startActivity(intent)
                overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
            }
        }

        fun createIntent(context: Context, webViewNotice: WebViewNotice) =
            Intent(context, NoticeWebActivity::class.java).apply {
                putExtra(WebViewNotice.EXTRA_KEY, webViewNotice)
            }

        fun createIntent(
            context: Context,
            url: String?,
            articleId: String?,
            id: Int?,
            category: String?,
            subject: String?,
        ): Intent {
            if (url == null || articleId == null || category == null || id == null) {
                throw IllegalArgumentException("intent parameters shouldn't be null: $url, $articleId, $id, $category")
            }
            return createIntent(
                context,
                WebViewNotice(url, articleId, id, category, subject.orEmpty())
            )
        }
    }
}
