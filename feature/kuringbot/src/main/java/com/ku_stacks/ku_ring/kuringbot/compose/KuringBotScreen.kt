package com.ku_stacks.ku_ring.kuringbot.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.kuringbot.KuringBotUIMessage
import com.ku_stacks.ku_ring.kuringbot.KuringBotUIState
import com.ku_stacks.ku_ring.kuringbot.KuringBotViewModel
import com.ku_stacks.ku_ring.kuringbot.R
import com.ku_stacks.ku_ring.kuringbot.compose.components.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@Composable
internal fun KuringBotScreen(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: KuringBotViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    KuringBotScreen(
        uiState = state,
        onBackButtonClick = onBackButtonClick,
        onQuestionValueChange = viewModel::onQuestionUpdate,
        onSendIconClick = { viewModel.setSendQuestionDialogVisibility(true) },
        onStopIconClick = viewModel::stopReceivingResponse,
        onSendQuestion = {
            viewModel.setSendQuestionDialogVisibility(false)
            viewModel.sendQuestion()
        },
        onHideQuestionDialog = { viewModel.setSendQuestionDialogVisibility(false) },
        modifier = modifier,
    )
}

@Composable
private fun KuringBotScreen(
    uiState: KuringBotUIState,
    onBackButtonClick: () -> Unit,
    onQuestionValueChange: (String) -> Unit,
    onSendIconClick: () -> Unit,
    onStopIconClick: () -> Unit,
    onSendQuestion: () -> Unit,
    onHideQuestionDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isInfoDialogVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KuringBotTopBar(
                onBackIconClick = onBackButtonClick,
                onInfoIconClick = { isInfoDialogVisible = !isInfoDialogVisible },
            )
        },
        modifier = modifier.fillMaxSize(),
        backgroundColor = KuringTheme.colors.background,
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            KuringBotScreenContents(
                uiState = uiState,
                onQuestionValueChange = onQuestionValueChange,
                onSendIconClick = onSendIconClick,
                onStopIconClick = onStopIconClick,
            )

            KuringBotInfoDialog(
                isVisible = isInfoDialogVisible,
                onDismiss = { isInfoDialogVisible = false },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    if (uiState.isSendQuestionDialogVisible) {
        SendQuestionDialog(
            onSend = onSendQuestion,
            onCancel = onHideQuestionDialog,
        )
    }
}

@Composable
private fun KuringBotScreenContents(
    uiState: KuringBotUIState,
    onQuestionValueChange: (String) -> Unit,
    onSendIconClick: () -> Unit,
    onStopIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.messages.isEmpty()) {
            NoMessageIndicator(modifier = Modifier.weight(1f))
        } else {
            KuringBotMessages(uiState.messages, modifier = Modifier.weight(1f))
        }

        KuringBotQuestionInput(
            question = uiState.question,
            onQuestionValueChange = onQuestionValueChange,
            onSendIconClick = onSendIconClick,
            onStopIconClick = onStopIconClick,
            isReceivingResponse = uiState.isReceivingResponse,
        )

        Text(
            text = stringResource(id = R.string.kuringbot_disclaimer),
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 19.56.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption2,
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )
    }
}

@Composable
private fun KuringBotTopBar(
    onBackIconClick: () -> Unit,
    onInfoIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterTitleTopBar(
        navigation = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_v2),
                contentDescription = null,
                modifier = Modifier.rotate(180f),
            )
        },
        onNavigationClick = onBackIconClick,
        navigationClickLabel = stringResource(id = R.string.kuringbot_back_button),
        title = stringResource(id = R.string.kuringbot_title),
        action = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_info_circle_mono_v2),
                contentDescription = null,
                tint = KuringTheme.colors.gray200,
            )
        },
        onActionClick = onInfoIconClick,
        actionClickLabel = stringResource(id = R.string.kuringbot_info_button),
        modifier = modifier,
    )
}

@Composable
private fun KuringBotQuestionInput(
    question: String,
    onQuestionValueChange: (String) -> Unit,
    onSendIconClick: () -> Unit,
    onStopIconClick: () -> Unit,
    isReceivingResponse: Boolean,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        KuringBotTextField(
            query = question,
            onQueryUpdate = onQuestionValueChange,
            modifier = Modifier.weight(1f),
        )

        SendOrStopButton(
            isReceivingResponse = isReceivingResponse,
            onSendIconClick = onSendIconClick,
            onStopIconClick = onStopIconClick,
            enabled = isReceivingResponse || question.isNotEmpty(),
        )
    }
}

@Composable
private fun SendOrStopButton(
    isReceivingResponse: Boolean,
    onSendIconClick: () -> Unit,
    onStopIconClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val iconId = sendOrStopIconId(isReceivingResponse)
    val contentDescriptionId = sendOrStopContentDescriptionId(isReceivingResponse)
    val onClick = if (isReceivingResponse) onStopIconClick else onSendIconClick

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(KuringTheme.colors.gray400)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconId),
            contentDescription = stringResource(contentDescriptionId),
            modifier = Modifier.padding(8.dp),
            tint = KuringTheme.colors.background,
        )
    }
}

@Composable
private fun sendOrStopIconId(isReceivingResponse: Boolean) = if (isReceivingResponse) {
    R.drawable.ic_stop_v2
} else {
    R.drawable.ic_arrow_up_v2
}

@Composable
private fun sendOrStopContentDescriptionId(isReceivingResponse: Boolean) =
    if (isReceivingResponse) {
        R.string.kuringbot_stop_response
    } else {
        R.string.kuringbot_send_query
    }

@Composable
private fun NoMessageIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_app_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray200,
            modifier = Modifier.size(84.dp),
        )
        Text(
            text = stringResource(id = R.string.kuringbot_empty_message),
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

@Composable
private fun KuringBotMessages(
    messages: List<KuringBotUIMessage>,
    modifier: Modifier = Modifier,
) {
    val reversedMessages = remember(messages) { messages.reversed() }

    LazyColumn(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        reverseLayout = true,
    ) {
        items(
            items = reversedMessages,
            contentType = { it.javaClass.name },
        ) { message ->
            KuringBotUIMessage(message)
        }
    }
}

@Composable
private fun KuringBotUIMessage(message: KuringBotUIMessage) {
    SelectionContainer {
        when (message) {
            KuringBotUIMessage.Loading -> {
                LoadingMessage()
            }

            is KuringBotUIMessage.Question -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    QuestionMessage(
                        message = message,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    )
                }
            }

            is KuringBotUIMessage.QuestionsRemaining -> {
                QuestionsRemainingMessage(message = message)
            }

            is KuringBotUIMessage.Response -> {
                ResponseMessage(message = message)
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun KuringBotScreenPreview() {
    var question by remember { mutableStateOf("예시 질문") }
    KuringTheme {
        KuringBotScreen(
            uiState = KuringBotUIState(
                question = question,
                messages = previewMessages,
                isSendQuestionDialogVisible = false,
                isReceivingResponse = false,
            ),
            onBackButtonClick = {},
            onQuestionValueChange = { question = it },
            onSendIconClick = {},
            onStopIconClick = {},
            onSendQuestion = {},
            onHideQuestionDialog = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

private val previewMessages: List<KuringBotUIMessage> = listOf(
    KuringBotUIMessage.Question(
        message = "예시 질문".repeat(10),
        postedTime = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 0),
    ),
    KuringBotUIMessage.Response(
        message = "예시 대답".repeat(10),
        postedTime = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 0),
    ),
    KuringBotUIMessage.Question(
        message = "예시 질문",
        postedTime = LocalDateTime.now(),
    ),
    KuringBotUIMessage.Response(
        message = "예시 대답",
        postedTime = LocalDateTime.now(),
    ),
    KuringBotUIMessage.QuestionsRemaining(
        questionsRemaining = 1,
        postedTime = LocalDate.now(),
    ),
    KuringBotUIMessage.Question(
        message = "예시 질문",
        postedTime = LocalDateTime.now(),
    ),
    KuringBotUIMessage.Response(
        message = "예시 대답",
        postedTime = LocalDateTime.now(),
    ),
    KuringBotUIMessage.QuestionsRemaining(
        questionsRemaining = 0,
        postedTime = LocalDate.now(),
    ),
)

@LightAndDarkPreview
@Composable
private fun KuringBotScreenPreview_Empty() {
    var question by remember { mutableStateOf("예시 질문") }
    KuringTheme {
        KuringBotScreen(
            uiState = KuringBotUIState(
                question = question,
                messages = emptyList(),
                isSendQuestionDialogVisible = false,
                isReceivingResponse = false,
            ),
            onBackButtonClick = {},
            onQuestionValueChange = { question = it },
            onSendIconClick = {},
            onStopIconClick = {},
            onSendQuestion = {},
            onHideQuestionDialog = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}