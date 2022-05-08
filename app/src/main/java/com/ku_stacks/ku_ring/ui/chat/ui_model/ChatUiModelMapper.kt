package com.ku_stacks.ku_ring.ui.chat.ui_model

import com.ku_stacks.ku_ring.util.DateUtil.convertLongToHHMM
import com.sendbird.android.SendbirdChat
import com.sendbird.android.message.AdminMessage
import com.sendbird.android.message.BaseMessage
import com.sendbird.android.message.UserMessage
import timber.log.Timber

fun List<BaseMessage>.toChatUiModelList(): List<ChatUiModel> {
    val currentUser = SendbirdChat.currentUser
    val chatUiModelList = mutableListOf<ChatUiModel>()

    if (currentUser == null) {
        Timber.e("currentUser is null!")
    }

    for(message in this) {
        if (message is AdminMessage) {
            chatUiModelList.add(message.toAdminMessageUiModel())
        }
        else if (message is UserMessage) {
            if (message.sender?.userId == currentUser?.userId) {
                chatUiModelList.add(message.toSentMessageUiModel())
            } else {
                chatUiModelList.add(message.toReceivedMessageUiModel())
            }
        }
        else {
            Timber.e("message type is strange! ${message.messageId}")
        }
    }
    return chatUiModelList
}

private fun UserMessage.toReceivedMessageUiModel(): ReceivedMessageUiModel {
    return ReceivedMessageUiModel(
        userId = this.sender?.userId ?: "",
        nickname = this.sender?.nickname ?: "",
        messageId = this.messageId,
        message = this.message,
        time = convertLongToHHMM(this.createdAt)
    )
}

private fun UserMessage.toSentMessageUiModel(): SentMessageUiModel {
    return SentMessageUiModel(
        messageId = this.messageId,
        message = this.message,
        time = convertLongToHHMM(this.createdAt)
    )
}

private fun AdminMessage.toAdminMessageUiModel(): AdminMessageUiModel {
    return AdminMessageUiModel(
        messageId = this.messageId,
        message = this.message,
        time = convertLongToHHMM(this.createdAt)
    )
}