package com.ku_stacks.ku_ring.feedback.feedback.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackTextStatus
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModelInterface
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CenterTitleTopBar(
            title = "피드백 보내기",
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor = MaterialTheme.colors.surface)
                )
            },
            onNavigationClick = {
                viewModel.closeFeedback()
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

        val textFieldModifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 32.dp)
            .fillMaxWidth()
            .weight(1f)
            .background(color = MaterialTheme.colors.surface)
            .border(width = 1.5f.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp))
        FeedbackTextField(viewModel, textFieldModifier)

        SendFeedbackButton(viewModel)
    }
}

@Composable
private fun FeedbackTextField(
    viewModel: FeedbackViewModelInterface,
    modifier: Modifier,
) {
    val text = viewModel.feedbackContent.collectAsState()
    val textStatus = viewModel.textStatus.collectAsState(FeedbackTextStatus.TOO_SHORT)
    
    Box(modifier = modifier) {
        TextField(
            value = text.value,
            onValueChange = { newText ->
                viewModel.updateFeedbackContent(newText)
            },
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text(
                    text = "최소 5글자 이상 작성해주세요",
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

        val guideTextInfo = when (textStatus.value) {
            FeedbackTextStatus.TOO_SHORT -> {
                Pair("5글자 이상 입력해주세요", Color.Red)
            }
            FeedbackTextStatus.TOO_LONG -> {
                Pair("${text.value.length}/${FeedbackViewModel.MAX_FEEDBACK_CONTENT_LENGTH}", Color.Red)
            }
            FeedbackTextStatus.NORMAL -> {
                Pair("${text.value.length}/${FeedbackViewModel.MAX_FEEDBACK_CONTENT_LENGTH}", KuringGreen)
            }
        }

        Text(
            text = guideTextInfo.first,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(400),
            color = guideTextInfo.second,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 6.dp, end = 13.dp)
        )
    }
}

@Composable
private fun SendFeedbackButton(
    viewModel: FeedbackViewModelInterface,
) {
    val textStatus = viewModel.textStatus.collectAsState(FeedbackTextStatus.TOO_SHORT)

    val backgroundColor = when (textStatus.value) {
        FeedbackTextStatus.NORMAL -> KuringGreen
        else -> KuringSecondaryGreen
    }
    val textColor = when (textStatus.value) {
        FeedbackTextStatus.NORMAL -> MaterialTheme.colors.surface
        else ->KuringGreen
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(color = backgroundColor)
            .clickable {
                viewModel.sendFeedback()
            }
    ) {
        Text(
            text = "피드백 보내기",
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
private fun FeedbackScreenPreView() {
    val viewModel: FeedbackViewModelInterface = object: FeedbackViewModelInterface {
        override val feedbackContent = MutableStateFlow("")
        override val textStatus = MutableStateFlow(FeedbackTextStatus.TOO_SHORT)
    }
    FeedbackScreen(viewModel)
}
