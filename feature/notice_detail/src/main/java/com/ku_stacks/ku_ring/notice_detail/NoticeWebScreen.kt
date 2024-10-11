package com.ku_stacks.ku_ring.notice_detail

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.KuringWebView
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.util.WordConverter

@Composable
fun NoticeWebScreen(
    webViewNotice: WebViewNotice,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeWebViewModel = hiltViewModel(),
) {
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    NoticeWebScreen(
        webViewNotice = webViewNotice,
        isSaved = isSaved,
        onNavigateBack = onNavigateBack,
        onSaveButtonClick = viewModel::onSaveButtonClick,
        doAfterPageLoaded = viewModel::updateNoticeTobeRead,
        modifier = modifier,
    )
}

@Composable
private fun NoticeWebScreen(
    webViewNotice: WebViewNotice,
    isSaved: Boolean,
    onNavigateBack: () -> Unit,
    onSaveButtonClick: () -> Unit,
    doAfterPageLoaded: (WebViewNotice) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(webViewNotice) {
        doAfterPageLoaded(webViewNotice)
    }

    Scaffold(
        topBar = {
            CenterTitleTopBar(
                title = WordConverter.convertEnglishToKorean(webViewNotice.category),
                modifier = Modifier.fillMaxWidth(),
                navigation = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_v2),
                        contentDescription = stringResource(id = R.string.navigation_button_description),
                    )
                },
                onNavigationClick = onNavigateBack,
                action = {
                    NoticeWebScreenActions(
                        isSaved = isSaved,
                        onSaveButtonClick = onSaveButtonClick,
                        webViewNotice = webViewNotice,
                    )
                }
            )
        },
        modifier = modifier,
    ) {
        KuringWebView(
            url = webViewNotice.url,
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            customSettings = {
                builtInZoomControls = true
            },
        )
    }
}

@Composable
private fun NoticeWebScreenActions(
    isSaved: Boolean,
    onSaveButtonClick: () -> Unit,
    webViewNotice: WebViewNotice,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = if (isSaved) R.drawable.ic_bookmark_fill_v2 else R.drawable.ic_bookmark_v2),
            contentDescription = stringResource(id = R.string.save_button_description),
            tint = KuringTheme.colors.gray600,
            modifier = Modifier
                .clickable(role = Role.Switch, onClick = onSaveButtonClick),
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_share_v2),
            contentDescription = stringResource(id = R.string.share_externally_description),
            tint = KuringTheme.colors.gray600,
            modifier = Modifier.clickable(
                role = Role.Button,
                onClick = { onShareButtonClick(context, webViewNotice.url) }
            ),
        )
    }
}

private fun onShareButtonClick(context: Context, url: String) {
    ShareCompat.IntentBuilder(context)
        .setChooserTitle(R.string.share_externally_description)
        .setText(url)
        .setType("text/plain")
        .startChooser()
}

@LightAndDarkPreview
@Composable
private fun NoticeWebScreenPreview() {
    var isSaved by remember { mutableStateOf(false) }
    KuringTheme {
        NoticeWebScreen(
            webViewNotice = WebViewNotice(
                url = "https://www.google.com",
                articleId = "123",
                category = "학사",
                subject = "쿠링 발전의 건에 대하여",
            ),
            isSaved = isSaved,
            onNavigateBack = {},
            onSaveButtonClick = { isSaved = !isSaved },
            doAfterPageLoaded = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}