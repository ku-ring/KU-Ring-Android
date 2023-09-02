package com.ku_stacks.ku_ring.ui.notion

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R

class NotionViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        val url = intent.getStringExtra(NOTION_URL)

        val webView = findViewById<WebView>(R.id.personal_data_webview)
        val progressBar = findViewById<ProgressBar>(R.id.personal_data_progressbar)

        webView.settings.apply {
            builtInZoomControls = false
            domStorageEnabled = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            setSupportZoom(false)
        }

        // WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
                super.onProgressChanged(view, newProgress)
            }
        }
        url?.let {
            webView.loadUrl(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val NOTION_URL = "notion_url"
        fun start(activity: Activity, notionUrl: String) {
            val intent = Intent(activity, NotionViewActivity::class.java).apply {
                putExtra(NOTION_URL, notionUrl)
            }
            activity.startActivity(intent)
        }
    }
}