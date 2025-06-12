package com.ku_stacks.ku_ring.notice_detail.noticeweb

import android.content.Context
import android.widget.Toast
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
import com.ku_stacks.ku_ring.designsystem.components.KuringAlertDialog
import com.ku_stacks.ku_ring.designsystem.components.KuringWebView
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.NoticeComment
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.notice_detail.R
import com.ku_stacks.ku_ring.notice_detail.component.CommentsBottomSheet
import com.ku_stacks.ku_ring.util.WordConverter
import kotlinx.coroutines.launch

@Composable
fun NoticeWebScreen(
    webViewNotice: WebViewNotice,
    onNavigateBack: () -> Unit,
    onReportComment: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeWebViewModel = hiltViewModel(),
) {
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
    val commentPager by viewModel.commentsPager.collectAsStateWithLifecycle()
    val replyCommentId by viewModel.replyCommentId.collectAsStateWithLifecycle()
    val deleteCommentId by viewModel.deleteCommentId.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val makeToast: (String) -> Unit = {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    val onCreateCommentSuccessMessage = stringResource(R.string.comment_bottom_sheet_create_success)
    val onCreateCommentFailMessage = stringResource(R.string.comment_bottom_sheet_create_fail)

    val onDeleteCommentSuccessMessage = stringResource(R.string.comment_bottom_sheet_delete_success)
    val onDeleteCommentFailMessage = stringResource(R.string.comment_bottom_sheet_delete_fail)

    NoticeWebScreen(
        webViewNotice = webViewNotice,
        isSaved = isSaved,
        onNavigateBack = onNavigateBack,
        onSaveButtonClick = viewModel::onSaveButtonClick,
        doAfterPageLoaded = viewModel::updateNoticeTobeRead,
        commentPager = commentPager,
        onCommentSheetOpen = viewModel::onCommentBottomSheetOpen,
        onCreateComment = { comment ->
            viewModel.createComment(
                comment = comment,
                onSuccess = { makeToast(onCreateCommentSuccessMessage) },
                onFailure = { message -> makeToast(message ?: onCreateCommentFailMessage) },
            )
        },
        setReplyCommentId = viewModel::setReplyCommentId,
        replyCommentId = replyCommentId,
        onReportComment = onReportComment,
        deleteCommentId = deleteCommentId,
        deleteComment = {
            viewModel.deleteComment(
                onSuccess = { makeToast(onDeleteCommentSuccessMessage) },
                onFailure = { makeToast(onDeleteCommentFailMessage) }
            )
        },
        onShowDeleteCommentPopup = viewModel::showDeleteCommentPopup,
        onHideDeleteCommentPopup = viewModel::hideDeleteCommentPopup,
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
    commentPager: Pager<Int, NoticeComment>?,
    onCommentSheetOpen: () -> Unit,
    onCreateComment: (String) -> Unit,
    setReplyCommentId: (Int?) -> Unit,
    replyCommentId: Int?,
    onReportComment: (Int) -> Unit,
    deleteCommentId: Int?,
    deleteComment: () -> Unit,
    onShowDeleteCommentPopup: (Int) -> Unit,
    onHideDeleteCommentPopup: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(webViewNotice) {
        doAfterPageLoaded(webViewNotice)
    }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isBottomSheetVisible by rememberUpdatedState(newValue = bottomSheetState.isVisible || bottomSheetState.targetValue == SheetValue.Expanded)
    val commentPagingItems = commentPager?.flow?.collectAsLazyPagingItems()

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
                onDismissRequest = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                        setReplyCommentId(null)
                    }
                },
                sheetState = bottomSheetState,
                containerColor = KuringTheme.colors.background,
            ) {
                CommentsBottomSheet(
                    comments = commentPagingItems,
                    replyCommentId = replyCommentId,
                    onCreateComment = onCreateComment,
                    setReplyCommentId = setReplyCommentId,
                    onReportComment = onReportComment,
                    onDeleteComment = onShowDeleteCommentPopup,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    if (deleteCommentId != null) {
        KuringAlertDialog(
            text = stringResource(R.string.comment_bottom_sheet_dialog_text),
            onConfirm = {
                deleteComment()
                commentPagingItems?.refresh()
                onHideDeleteCommentPopup()
            },
            onCancel = { onHideDeleteCommentPopup() },
            confirmText = stringResource(R.string.comment_bottom_sheet_dialog_delete_text),
            cancelText = stringResource(R.string.comment_bottom_sheet_dialog_cancel_text),
            confirmTextColor = KuringTheme.colors.warning,
        )
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
            commentPager = null,
            onCommentSheetOpen = {},
            onCreateComment = {},
            setReplyCommentId = {},
            replyCommentId = null,
            onReportComment = {},
            deleteCommentId = null,
            deleteComment = {},
            onShowDeleteCommentPopup = {},
            onHideDeleteCommentPopup = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}