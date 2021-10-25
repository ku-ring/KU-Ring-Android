package com.ku_stacks.ku_ring.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
import android.webkit.WebSettings

import android.webkit.WebViewClient




class DetailActivity: AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val url = intent.getStringExtra("url")

        webView = findViewById(R.id.detail_webView)
        webView.webViewClient = WebViewClient() // 클릭시 새창 안뜨게

        webView.settings.apply {
            javaScriptEnabled = true // 웹페이지 자바스클비트 허용 여부
            setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
            javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
            loadWithOverviewMode = true // 메타태그 허용 여부
            useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
            setSupportZoom(false) // 화면 줌 허용 여부
            builtInZoomControls = false // 화면 확대 축소 허용 여부
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠 사이즈 맞추기
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // 브라우저 캐시 허용 여부
            domStorageEnabled = true // 로컬저장소 허용 여부
        }

        url?.let {
            webView.loadUrl(it) //웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        }
    }
}