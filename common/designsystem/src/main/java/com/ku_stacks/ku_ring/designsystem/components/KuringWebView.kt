package com.ku_stacks.ku_ring.designsystem.components

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard


@Composable
fun KuringWebView(
    url: String?,
    modifier: Modifier = Modifier,
    customSettings: WebSettings.() -> Unit = {},
) {
    // Preview에서 android view를 지원하지 않으므로, 프리뷰가 아닐 때에만 웹뷰를 보여주도록 설정
    if (url != null && !LocalInspectionMode.current) {
        KuringWebView(
            url = url,
            customSettings = customSettings,
            modifier = modifier,
        )
    } else {
        KuringWebViewErrorScreen(modifier = modifier)
    }
}

@JvmName("KuringWebViewUrlNotNull")
@Composable
private fun KuringWebView(
    url: String,
    customSettings: WebSettings.() -> Unit,
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        if (progress != 100) {
            LinearProgressIndicator(
                progress = progress / 100f,
                color = KuringTheme.colors.mainPrimary,
                trackColor = KuringTheme.colors.background,
            )
        }
        AndroidView(
            factory = { context ->
                createWebView(
                    context = context,
                    onSetProgress = { progress = it },
                    customSettings = customSettings,
                )
            },
            update = {
                it.loadUrl(url)
            },
        )
    }
}

private fun createWebView(
    context: Context,
    onSetProgress: (Int) -> Unit,
    customSettings: WebSettings.() -> Unit = {},
) = WebView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            onSetProgress(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
    settings.apply {
        builtInZoomControls = false
        domStorageEnabled = true
        javaScriptEnabled = true
        loadWithOverviewMode = true
        blockNetworkLoads = false
        setSupportZoom(false)
        customSettings()
    }
}

@Composable
private fun KuringWebViewErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(KuringTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_construction),
            contentDescription = null,
            modifier = Modifier.size(98.dp),
        )
        Text(
            text = stringResource(id = R.string.webview_url_null),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.45.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption1,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@LightAndDarkPreview
@Composable
private fun KuringWebViewPreview_urlNull() {
    KuringTheme {
        KuringWebView(
            url = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}