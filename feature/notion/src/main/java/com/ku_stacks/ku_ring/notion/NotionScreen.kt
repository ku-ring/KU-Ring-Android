package com.ku_stacks.ku_ring.notion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.components.KuringWebView
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.KuringIconTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
fun NotionScreen(
    url: String?,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            KuringIconTopBar(modifier = Modifier.fillMaxWidth())
        },
        modifier = modifier,
    ) {
        val innerModifier = Modifier
            .padding(it)
            .fillMaxSize()
        KuringWebView(
            url = url,
            modifier = innerModifier,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NotionScreenTest() {
    KuringTheme {
        // 이 Preview를 run하면 INTERNET permission 에러가 발생한다. 프리뷰용 앱에는 권한이 없기 때문.
        // 앱을 직접 실행해서 확인하는 것이 좋다.
        NotionScreen(
            url = "https://kuring.notion.site/kuring/eec1169151ec4b49abab5e900c617690",
            modifier = Modifier.fillMaxSize(),
        )
    }
}