package com.ku_stacks.ku_ring.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
import timber.log.Timber

class DetailActivity: AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val url = intent.getStringExtra("url")
        Timber.e("detail url : $url")

        webView = findViewById(R.id.detail_webView)
        webView.webViewClient = WebViewClient() // 클릭시 새창 안뜨게

        progressBar = findViewById(R.id.detail_progressbar)

        webView.settings.apply {
//            javaScriptEnabled = true // 웹페이지 자바스크립트 허용 여부
//            setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
//            javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//            loadWithOverviewMode = true // 메타태그 허용 여부
//            useWideViewPort = false // 화면 사이즈 맞추기 허용 여부(true로 두면 pc화면 처럼 보임)
//            setSupportZoom(false) // 화면 줌 허용 여부
//            displayZoomControls = false // 화면 줌 허용할 때 돋보기 보임 여부
//            builtInZoomControls = true // 화면 확대 축소 허용 여부 (true로 두면 돋보기+- 버튼 생김)
//            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠 사이즈 맞추기
//            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // 브라우저 캐시 허용 여부
//            domStorageEnabled = true // 로컬저장소 허용 여부
            //위의 설정으로 하면 일부 안열리는 링크가 있었음 ex. 예술문화관(능동로 방향) 펜스 출입문 통제 안내

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
            webView.loadUrl(it) //웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}