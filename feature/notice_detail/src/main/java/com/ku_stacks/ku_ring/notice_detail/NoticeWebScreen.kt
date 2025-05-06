package com.ku_stacks.ku_ring.notice_detail

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.KuringWebView
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.notice_detail.component.CommentsBottomSheet
import com.ku_stacks.ku_ring.util.WordConverter
import kotlinx.coroutines.launch

@Composable
fun NoticeWebScreen(
    webViewNotice: WebViewNotice,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeWebViewModel = hiltViewModel(),
) {
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
    val commentPager by viewModel.commentsPager.collectAsStateWithLifecycle()

    NoticeWebScreen(
        webViewNotice = webViewNotice,
        isSaved = isSaved,
        onNavigateBack = onNavigateBack,
        onSaveButtonClick = viewModel::onSaveButtonClick,
        doAfterPageLoaded = viewModel::updateNoticeTobeRead,
        commentsPager = commentPager,
        onCommentSheetOpen = viewModel::onCommentBottomSheetOpen,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoticeWebScreen(
    webViewNotice: WebViewNotice,
    isSaved: Boolean,
    onNavigateBack: () -> Unit,
    onSaveButtonClick: () -> Unit,
    doAfterPageLoaded: (WebViewNotice) -> Unit,
    commentsPager: Pager<Int, NoticeComment>?,
    onCommentSheetOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(webViewNotice) {
        doAfterPageLoaded(webViewNotice)
    }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isBottomSheetVisible by rememberUpdatedState(newValue = bottomSheetState.isVisible || bottomSheetState.targetValue == SheetValue.Expanded)

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
        floatingActionButton = {
            NoticeWebScreenFab(
                onClick = {
                    onCommentSheetOpen()
                    coroutineScope.launch { bottomSheetState.show() }
                },
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

        if (isBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { coroutineScope.launch { bottomSheetState.hide() } },
                sheetState = bottomSheetState,
                containerColor = KuringTheme.colors.background,
            ) {
                CommentsBottomSheet(
                    comments = commentsPager?.flow?.collectAsLazyPagingItems(),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun NoticeWebScreenFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.see_comment)
    val fabSize = 72.dp

    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .size(fabSize)
            .clearAndSetSemantics {
                contentDescription = description
            },
        containerColor = KuringTheme.colors.mainPrimary,
        contentColor = Color.White, // White in both light and dark mode
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_message_circle_v2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.8f)
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
                id = 1234,
                category = "학사",
                subject = "쿠링 발전의 건에 대하여",
            ),
            isSaved = isSaved,
            onNavigateBack = {},
            onSaveButtonClick = { isSaved = !isSaved },
            doAfterPageLoaded = {},
            commentsPager = null,
            onCommentSheetOpen = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}