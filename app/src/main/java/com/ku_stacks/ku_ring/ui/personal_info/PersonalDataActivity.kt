package com.ku_stacks.ku_ring.ui.personal_info

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R

class PersonalDataActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        val webView = findViewById<WebView>(R.id.personal_data_webview)
        val progressBar = findViewById<ProgressBar>(R.id.personal_data_progressbar)

        val settings = webView.settings
        settings.builtInZoomControls = false
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)

        // WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
                super.onProgressChanged(view, newProgress)
            }
        }

        webView.loadUrl(url)
    }

    companion object {
        const val url = "https://knowing-bamboo-7b4.notion.site/289d8db70bad49cd8ba80ed011281dfc"
    }
}