package com.ku_stacks.ku_ring.ui.notice_webview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityNoticeWebBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NoticeWebActivity : AppCompatActivity() {

    private val viewModel by viewModels<NoticeWebViewModel>()
    private lateinit var binding: ActivityNoticeWebBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(NOTICE_URL)
            ?: throw IllegalStateException("Web Link should not be null.")
        val articleId = intent.getStringExtra(NOTICE_ARTICLE_ID)
        val category = intent.getStringExtra(NOTICE_CATEGORY)
        Timber.e("notice url : $url")

        binding.noticeBackBt.setOnClickListener { finish() }

        binding.noticeShareBt.setOnClickListener {
            shareLinkExternally(url)
        }

        binding.noticeWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = request.url
                Timber.e("request url : ${request.url}")
                startActivity(intent)
                return true
            }
        }

        binding.noticeWebView.settings.apply {
            builtInZoomControls = true
            domStorageEnabled = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            displayZoomControls = false
        }

        // WebChromeClient
        binding.noticeWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                binding.noticeProgressbar.progress = newProgress
                if (newProgress == 100) {
                    updateNoticeTobeRead(articleId, category)
                    binding.noticeProgressbar.visibility = View.GONE
                    binding.noticeWebView.webChromeClient = null
                } else {
                    binding.noticeProgressbar.visibility = View.VISIBLE
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        binding.noticeWebView.loadUrl(url)
    }

    private fun updateNoticeTobeRead(articleId: String?, category: String?) {
        if (articleId.isNullOrEmpty() || category.isNullOrEmpty()) {
            Timber.e("articleId or category is null. articleId : $articleId, category : $category")
        } else {
            viewModel.updateNoticeTobeRead(articleId, category)
        }
    }

    private fun shareLinkExternally(url: String) {
        ShareCompat.IntentBuilder(this)
            .setChooserTitle(R.string.share_externally)
            .setText(url)
            .setType("text/plain")
            .startChooser()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val NOTICE_URL = "url"
        const val NOTICE_ARTICLE_ID = "articleId"
        const val NOTICE_CATEGORY = "category"
        const val NOTICE_POSTED_DATE = "postedDate"
        const val NOTICE_SUBJECT = "subject"
    }
}