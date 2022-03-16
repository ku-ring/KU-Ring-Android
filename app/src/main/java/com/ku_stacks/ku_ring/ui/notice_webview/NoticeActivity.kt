package com.ku_stacks.ku_ring.ui.notice_webview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NoticeActivity : AppCompatActivity() {

    private val viewModel by viewModels<NoticeViewModel>()

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: ImageButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        webView = findViewById(R.id.notice_webView)
        progressBar = findViewById(R.id.notice_progressbar)
        backButton = findViewById(R.id.notice_back_bt)

        val url = intent.getStringExtra(NOTICE_URL)
        val articleId = intent.getStringExtra(NOTICE_ARTICLE_ID)
        val category = intent.getStringExtra(NOTICE_CATEGORY)

        Timber.e("notice url : $url")

        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }

        webView.webViewClient = object : WebViewClient() {
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

        webView.settings.apply {
            builtInZoomControls = true
            domStorageEnabled = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            displayZoomControls = false
        }

        // WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                    updateNoticeTobeRead(articleId, category)
                    webView.webChromeClient = null
                } else {
                    progressBar.visibility = View.VISIBLE
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        url?.let {
            webView.loadUrl(it) //웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        }
    }

    private fun updateNoticeTobeRead(articleId: String?, category: String?) {
        if (articleId.isNullOrEmpty() || category.isNullOrEmpty()) {
            Timber.e("articleId or category is null. articleId : $articleId, category : $category")
        } else {
            viewModel.updateNoticeTobeRead(articleId, category)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val NOTICE_URL = "url"
        const val NOTICE_ARTICLE_ID = "articleId"
        const val NOTICE_CATEGORY = "category"
    }
}