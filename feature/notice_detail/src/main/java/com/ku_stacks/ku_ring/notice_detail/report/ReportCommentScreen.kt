package com.ku_stacks.ku_ring.notice_detail.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.notice_detail.R
import com.ku_stacks.ku_ring.ui_util.rememberKeyboardVisibilityState
import com.ku_stacks.ku_ring.ui_util.showToast
import kotlinx.coroutines.launch

// TODO: :feature:feedback과 통일하기

@Composable
internal fun ReportCommentScreen(
    reportCommentId: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReportCommentViewModel = hiltViewModel(),
) {
    val reportContent = viewModel.reportContent

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    ReportCommentScreen(
        reportContent = reportContent,
        textStatus = viewModel.textStatus,
        onClickClose = onClose,
        onTextFieldUpdate = viewModel::updateReportContent,
        onReportSubmit = {
            scope.launch {
                viewModel.report(
                    commentId = reportCommentId,
                    content = reportContent,
                    onSuccess = {
                        context.showToast(R.string.comment_report_toast_success)
                        onClose()
                    },
                    onFailure = {
                        context.showToast(it)
                    },
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ReportCommentScreen(
    reportContent: String,
    textStatus: ReportCommentTextStatus,
    onClickClose: () -> Unit,
    onTextFieldUpdate: (String) -> Unit,
    onReportSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isKeyboardOpen by rememberKeyboardVisibilityState()

    val scrollState = rememberScrollState()
    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen) {
            scrollState.animateScrollTo(Int.MAX_VALUE)
        }
    }

    Scaffold(
        topBar = {
            CenterTitleTopBar(
                title = stringResource(R.string.comment_report_title),
                navigation = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_v2),
                        contentDescription = null,
                        tint = KuringTheme.colors.gray600,
                    )
                },
                onNavigationClick = onClickClose,
                action = "",
            )
        },
        modifier = modifier.background(KuringTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(KuringTheme.colors.background)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_construction),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 7.dp)
                    .width(160.dp)
                    .height(160.dp)
            )
            FeedbackTextField(
                feedbackContent = reportContent,
                textStatus = textStatus,
                onTextFieldUpdate = onTextFieldUpdate,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 32.dp)
                    .fillMaxWidth()
                    .heightIn(max = 268.dp)
                    .background(color = KuringTheme.colors.background)
                    .border(width = 1.5f.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
            )
            if (!isKeyboardOpen) {
                Spacer(modifier = Modifier.weight(1f))
            }
            KuringCallToAction(
                text = stringResource(id = R.string.comment_report_cta),
                onClick = onReportSubmit,
                enabled = textStatus == ReportCommentTextStatus.NORMAL,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun FeedbackTextField(
    feedbackContent: String,
    textStatus: ReportCommentTextStatus,
    onTextFieldUpdate: (String) -> Unit,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        TextField(
            value = feedbackContent,
            onValueChange = { newText ->
                onTextFieldUpdate(newText)
            },
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.comment_report_placeholder),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(400),
                    color = KuringTheme.colors.textBody.copy(alpha = 0.5f),
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = KuringTheme.colors.textBody,
                unfocusedTextColor = KuringTheme.colors.textBody,
                disabledTextColor = Color.Transparent,
                errorTextColor = KuringTheme.colors.warning,
                focusedContainerColor = KuringTheme.colors.background,
                unfocusedContainerColor = KuringTheme.colors.background,
                cursorColor = KuringTheme.colors.mainPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 6.dp)
        )

        val (guideTextInfo, color) = when (textStatus) {
            ReportCommentTextStatus.INITIAL -> {
                Pair("", KuringTheme.colors.warning)
            }

            ReportCommentTextStatus.TOO_SHORT -> {
                Pair(stringResource(R.string.comment_report_too_short), KuringTheme.colors.warning)
            }

            ReportCommentTextStatus.TOO_LONG -> {
                Pair(
                    "${feedbackContent.length}/${ReportCommentViewModel.MAX_REPORT_CONTENT_LENGTH}",
                    KuringTheme.colors.warning
                )
            }

            ReportCommentTextStatus.NORMAL -> {
                Pair(
                    "${feedbackContent.length}/${ReportCommentViewModel.MAX_REPORT_CONTENT_LENGTH}",
                    KuringTheme.colors.mainPrimary
                )
            }
        }

        Text(
            text = guideTextInfo,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(400),
            color = color,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 6.dp, end = 13.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ReportCommentScreenPreview() {
    var content by remember { mutableStateOf("안녕하세요") }
    KuringTheme {
        ReportCommentScreen(
            reportContent = content,
            textStatus = ReportCommentTextStatus.NORMAL,
            onReportSubmit = {},
            onClickClose = {},
            onTextFieldUpdate = { content = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
