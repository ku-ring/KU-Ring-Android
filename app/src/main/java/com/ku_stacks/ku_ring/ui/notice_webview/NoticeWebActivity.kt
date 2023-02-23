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
import com.ku_stacks.ku_ring.data.mapper.concatSubjectAndTag
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.ActivityNoticeWebBinding
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.util.UrlGenerator
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

        val url = intent.getStringExtra(NOTICE_URL)
            ?: throw IllegalStateException("Web Link should not be null.")
        val articleId = intent.getStringExtra(NOTICE_ARTICLE_ID)
        val category = intent.getStringExtra(NOTICE_CATEGORY)
        Timber.e("notice url : $url")

        binding.noticeBackBt.setOnClickListener { finish() }

        binding.noticeShareBt.setOnClickListener {
            shareLinkExternally(url)
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
        const val NOTICE_URL = "url"
        const val NOTICE_ARTICLE_ID = "articleId"
        const val NOTICE_CATEGORY = "category"
        const val NOTICE_POSTED_DATE = "postedDate"
        const val NOTICE_SUBJECT = "subject"

        fun createIntent(context: Context, notice: Notice) = createIntent(
            context,
            notice.url,
            notice.articleId,
            notice.category,
            notice.postedDate,
            concatSubjectAndTag(notice.subject, notice.tag)
        )

        fun createIntent(context: Context, pushContent: PushContentUiModel): Intent {
            with(pushContent) {
                val url = UrlGenerator.generateNoticeUrl(articleId, category, baseUrl)
                return createIntent(
                    context,
                    url,
                    articleId,
                    category,
                    postedDate,
                    concatSubjectAndTag(subject, tag)
                )
            }
        }

        fun createIntent(
            context: Context,
            url: String?,
            articleId: String?,
            category: String?,
            postedDate: String?,
            subject: String?
        ) = Intent(context, NoticeWebActivity::class.java).apply {
            Timber.e("url: $url, category: $category")

            putExtra(NOTICE_URL, url)
            putExtra(NOTICE_ARTICLE_ID, articleId)
            putExtra(NOTICE_CATEGORY, category)
            putExtra(NOTICE_POSTED_DATE, postedDate)
            putExtra(NOTICE_SUBJECT, subject)
        }
    }
}