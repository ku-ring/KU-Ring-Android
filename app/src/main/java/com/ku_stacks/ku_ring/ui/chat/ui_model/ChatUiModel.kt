package com.ku_stacks.ku_ring.ui.chat.ui_model

sealed class ChatUiModel

data class ChatDateUiModel(
    val date: String
) : ChatUiModel()

data class ReceivedMessageUiModel(
    val userId: String,
    val nickname: String,
    val messageId: Long,
    val message: String,
    val time: String
) : ChatUiModel()

data class SentMessageUiModel(
    val messageId: Long,
    val message: String,
    val time: String
) : ChatUiModel()

data class AdminMessageUiModel(
    val messageId: Long,
    val message: String,
    val time: String
) : ChatUiModel()