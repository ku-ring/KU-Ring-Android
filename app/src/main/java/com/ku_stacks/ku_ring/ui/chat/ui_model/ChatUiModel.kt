package com.ku_stacks.ku_ring.ui.chat.ui_model

sealed class ChatUiModel(
    open val timeStamp: Long,
    open val messageId: Long?
)

data class ChatDateUiModel(
    override val timeStamp: Long
) : ChatUiModel(timeStamp, null)

data class ReceivedMessageUiModel(
    val userId: String,
    val nickname: String,
    override val messageId: Long,
    val message: String,
    override val timeStamp: Long
) : ChatUiModel(timeStamp, messageId)

data class SentMessageUiModel(
    override val messageId: Long,
    val message: String,
    override val timeStamp: Long
) : ChatUiModel(timeStamp, messageId)

data class AdminMessageUiModel(
    override val messageId: Long,
    val message: String,
    override val timeStamp: Long,
) : ChatUiModel(timeStamp, messageId)