package com.ku_stacks.ku_ring.ui.chat.ui_model

sealed class ChatUiModel(
    open val timeStamp: Long,
    open val messageId: Long?
)

data class ChatDateUiModel(
    override val timeStamp: Long
) : ChatUiModel(timeStamp, null)

data class ReceivedMessageUiModel(
    override val timeStamp: Long,
    override val messageId: Long,
    val userId: String,
    val nickname: String,
    val message: String
) : ChatUiModel(timeStamp, messageId)

data class SentMessageUiModel(
    override val timeStamp: Long,
    override val messageId: Long,
    val message: String,
    val requestId: String,
    val isPending: Boolean?
) : ChatUiModel(timeStamp, messageId)

data class AdminMessageUiModel(
    override val timeStamp: Long,
    override val messageId: Long,
    val message: String
) : ChatUiModel(timeStamp, messageId)