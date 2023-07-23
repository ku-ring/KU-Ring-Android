package com.ku_stacks.ku_ring.ui.notice_webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.mapper.toWebViewNotice
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.model.WebViewNotice
import com.ku_stacks.ku_ring.databinding.ActivityNoticeWebBinding
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

        // Deprecated되지 않은 다른 함수가 API 33 이상에서만 사용할 수 있어서 부득이하게 deprecated 함수를 사용
        val webViewNotice = intent.getSerializableExtra(WEB_VIEW_NOTICE) as? WebViewNotice
            ?: throw IllegalStateException("WebViewNotice should not be null.")
        Timber.e("web view notice: $webViewNotice")

        binding.noticeBackBt.setOnClickListener { finish() }

        binding.noticeShareBt.setOnClickListener {
            shareLinkExternally(webViewNotice.url)
        }

        binding.noticeSaveButton.setOnClickListener { viewModel.onSaveButtonClick() }

        collectSavedStatus()

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
                    updateNoticeTobeRead(webViewNotice.articleId, webViewNotice.category)
                    binding.noticeProgressbar.visibility = View.GONE
                    binding.noticeWebView.webChromeClient = null
                } else {
                    binding.noticeProgressbar.visibility = View.VISIBLE
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        binding.noticeWebView.loadUrl(webViewNotice.url)
    }

    private fun collectSavedStatus() {
        lifecycleScope.launchWhenResumed {
            viewModel.isSaved.collectLatest { isSaved ->
                binding.isNoticeSaved = isSaved
            }
        }
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
        const val WEB_VIEW_NOTICE = "webview_notice"

        fun createIntent(context: Context, notice: Notice) =
            createIntent(context, notice.toWebViewNotice())

        fun createIntent(context: Context, pushContent: PushContentUiModel) =
            createIntent(context, pushContent.toWebViewNotice())

        fun createIntent(
            context: Context,
            url: String?,
            articleId: String?,
            category: String?,
        ): Intent {
            if (url == null || articleId == null || category == null) {
                throw IllegalArgumentException("intent parameters shouldn't be null: $url, $articleId, $category")
            }
            return createIntent(context, WebViewNotice(url, articleId, category))
        }

        fun createIntent(context: Context, webViewNotice: WebViewNotice) =
            Intent(context, NoticeWebActivity::class.java).apply {
                Timber.d("WebViewNotice: $webViewNotice")
                putExtra(WEB_VIEW_NOTICE, webViewNotice)
            }
    }
}