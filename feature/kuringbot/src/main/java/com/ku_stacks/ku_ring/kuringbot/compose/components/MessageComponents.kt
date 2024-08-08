package com.ku_stacks.ku_ring.kuringbot.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.kuringbot.KuringBotUIMessage
import com.ku_stacks.ku_ring.kuringbot.R
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
internal fun QuestionMessage(
    message: KuringBotUIMessage.Question,
    modifier: Modifier = Modifier,
) {
    BaseMessage(
        align = Alignment.CenterEnd,
        modifier = modifier,
    ) {
        MessageText(
            text = message.message,
            isQuestion = true,
            modifier = Modifier.weight(1f, fill = false)
        )
        MessageIcon(
            iconId = R.drawable.ic_user_mono_v2,
            tint = KuringTheme.colors.gray300,
            modifier = Modifier.background(KuringTheme.colors.gray100, CircleShape),
        )
    }
}

@Composable
internal fun ResponseMessage(
    message: KuringBotUIMessage.Response,
    modifier: Modifier = Modifier,
) {
    BaseMessage(
        align = Alignment.CenterStart,
        modifier = modifier
    ) {
        MessageIcon(iconId = R.drawable.ic_app_v2)
        MessageText(
            text = message.message,
            isQuestion = false,
        )
    }
}

@Composable
internal fun LoadingMessage(
    modifier: Modifier = Modifier,
) {
    val loadingAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.animation_loading))
    BaseMessage(
        align = Alignment.CenterStart,
        modifier = modifier
    ) {
        MessageIcon(iconId = R.drawable.ic_app_v2)
        LottieAnimation(
            composition = loadingAnimation,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.offset(x = (-32).dp), // 임시 offset
        )
    }
}

@Composable
internal fun QuestionsRemainingMessage(
    message: KuringBotUIMessage.QuestionsRemaining,
    modifier: Modifier = Modifier,
) {
    val color =
        if (message.questionsRemaining == 0) KuringTheme.colors.warning else KuringTheme.colors.textCaption1

    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = message.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 19.56.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = color
            ),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun BaseMessage(
    align: Alignment,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = align,
    ) {
        val maxWidth = maxWidth
        Row(
            modifier = Modifier
                .widthIn(max = maxWidth * 267 / 375f)
                .height(IntrinsicSize.Min)
                .semantics(mergeDescendants = true) {},
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}

@Composable
private fun MessageText(
    text: String,
    isQuestion: Boolean,
    modifier: Modifier = Modifier,
) {
    val background =
        if (isQuestion) KuringTheme.colors.gray100 else KuringTheme.colors.mainPrimarySelected
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(background),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.45.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textBody,
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun MessageIcon(
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    Box(
        modifier = modifier
            .border(1.dp, KuringTheme.colors.gray100, CircleShape)
            .size(36.dp)
            .padding(2.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconId),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun KuringBotMessagesPreview() {
    KuringTheme {
        Column(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            QuestionMessage(
                message = KuringBotUIMessage.Question(
                    message = "테스트 메시지",
                    postedTime = LocalDateTime.now(),
                ),
            )
            QuestionMessage(
                message = KuringBotUIMessage.Question(
                    message = "테스트 메시지".repeat(5),
                    postedTime = LocalDateTime.now(),
                ),
            )
            ResponseMessage(
                message = KuringBotUIMessage.Response(
                    message = "테스트 응답",
                    postedTime = LocalDateTime.now(),
                )
            )
            ResponseMessage(
                message = KuringBotUIMessage.Response(
                    message = "테스트 응답".repeat(5),
                    postedTime = LocalDateTime.now(),
                )
            )
            LoadingMessage()
            QuestionsRemainingMessage(
                message = KuringBotUIMessage.QuestionsRemaining(
                    questionsRemaining = 1,
                    postedTime = LocalDate.now()
                ),
            )
            QuestionsRemainingMessage(
                message = KuringBotUIMessage.QuestionsRemaining(
                    questionsRemaining = 0,
                    postedTime = LocalDate.now()
                ),
            )
        }
    }
}