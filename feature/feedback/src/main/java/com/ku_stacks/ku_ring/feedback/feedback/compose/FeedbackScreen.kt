package com.ku_stacks.ku_ring.feedback.feedback.compose

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
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackTextStatus
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackViewModel
import com.ku_stacks.ku_ring.ui_util.rememberKeyboardVisibilityState

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel,
    modifier: Modifier = Modifier,
) {
    val feedbackContent by viewModel.feedbackContent.collectAsStateWithLifecycle()
    val textStatus by viewModel.textStatus.collectAsStateWithLifecycle()

    FeedbackScreen(
        feedbackContent = feedbackContent,
        textStatus = textStatus,
        onClickClose = viewModel::closeFeedback,
        onTextFieldUpdate = viewModel::updateFeedbackContent,
        onClickSendFeedback = viewModel::sendFeedback,
        modifier = modifier,
    )
}

@Composable
private fun FeedbackScreen(
    feedbackContent: String,
    textStatus: FeedbackTextStatus,
    onClickClose: () -> Unit,
    onTextFieldUpdate: (String) -> Unit,
    onClickSendFeedback: () -> Unit,
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
                title = stringResource(R.string.feedback_send_content),
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
                painter = painterResource(R.drawable.writing_emoji),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 7.dp)
                    .width(160.dp)
                    .height(160.dp)
            )
            Text(
                text = stringResource(R.string.feedback_guide),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = KuringTheme.colors.textBody,
                    textAlign = TextAlign.Center,
                ),
            )
            FeedbackTextField(
                feedbackContent = feedbackContent,
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
                text = stringResource(id = R.string.feedback_send_content),
                onClick = onClickSendFeedback,
                enabled = textStatus == FeedbackTextStatus.NORMAL,
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
    textStatus: FeedbackTextStatus,
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
                    text = stringResource(R.string.feedback_hint),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(400),
                    color = KuringTheme.colors.textBody,
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = KuringTheme.colors.textBody,
                disabledTextColor = Color.Transparent,
                backgroundColor = KuringTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 6.dp)
        )

        val (guideTextInfo, color) = when (textStatus) {
            FeedbackTextStatus.INITIAL -> {
                Pair("", KuringTheme.colors.warning)
            }

            FeedbackTextStatus.TOO_SHORT -> {
                Pair(stringResource(R.string.feedback_write_more), KuringTheme.colors.warning)
            }

            FeedbackTextStatus.TOO_LONG -> {
                Pair(
                    "${feedbackContent.length}/${FeedbackViewModel.MAX_FEEDBACK_CONTENT_LENGTH}",
                    KuringTheme.colors.warning
                )
            }

            FeedbackTextStatus.NORMAL -> {
                Pair(
                    "${feedbackContent.length}/${FeedbackViewModel.MAX_FEEDBACK_CONTENT_LENGTH}",
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
private fun FeedbackScreenPreview() {
    KuringTheme {
        FeedbackScreen(
            feedbackContent = "안녕하세요",
            textStatus = FeedbackTextStatus.NORMAL,
            onClickSendFeedback = {},
            onClickClose = {},
            onTextFieldUpdate = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
