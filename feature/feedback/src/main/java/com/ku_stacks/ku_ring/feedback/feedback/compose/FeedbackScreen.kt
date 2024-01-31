package com.ku_stacks.ku_ring.feedback.feedback.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringGreen
import com.ku_stacks.ku_ring.designsystem.theme.KuringSecondaryGreen
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackTextStatus
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackViewModel

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel,
    modifier: Modifier = Modifier,
) {
    val feedbackContent by viewModel.feedbackContent.collectAsState()
    val textStatus by viewModel.textStatus.collectAsState()

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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(MaterialTheme.colors.surface),
    ) {
        CenterTitleTopBar(
            title = stringResource(R.string.feedback_send_content),
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor = MaterialTheme.colors.surface)
                )
            },
            onNavigationClick = {
                onClickClose()
            },
            action = "",
        )

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
                color = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
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
                .weight(1f)
                .background(color = MaterialTheme.colors.surface)
                .border(width = 1.5f.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
        )

        SendFeedbackButton(
            textStatus = textStatus,
            onClickSendFeedback = onClickSendFeedback,
        )
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
                    text = stringResource(R.string.feedback_write_more_character),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(400),
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
                disabledTextColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.surface,
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
                Pair("", Color.Red)
            }
            FeedbackTextStatus.TOO_SHORT -> {
                Pair(stringResource(R.string.feedback_write_more), Color.Red)
            }
            FeedbackTextStatus.TOO_LONG -> {
                Pair("${feedbackContent.length}/${FeedbackViewModel.MAX_FEEDBACK_CONTENT_LENGTH}", Color.Red)
            }
            FeedbackTextStatus.NORMAL -> {
                Pair("${feedbackContent.length}/${FeedbackViewModel.MAX_FEEDBACK_CONTENT_LENGTH}", KuringGreen)
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

@Composable
private fun SendFeedbackButton(
    textStatus: FeedbackTextStatus,
    onClickSendFeedback: () -> Unit,
) {

    val backgroundColor = when (textStatus) {
        FeedbackTextStatus.NORMAL -> KuringGreen
        else -> KuringSecondaryGreen
    }
    val textColor = when (textStatus) {
        FeedbackTextStatus.NORMAL -> MaterialTheme.colors.surface
        else -> KuringGreen
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(color = backgroundColor)
            .clickable {
                onClickSendFeedback()
            }
    ) {
        Text(
            text = stringResource(R.string.feedback_send_content),
            fontSize = 16.sp,
            lineHeight = 26.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = textColor,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp)
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
