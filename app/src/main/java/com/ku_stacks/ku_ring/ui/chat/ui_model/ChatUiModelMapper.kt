package com.ku_stacks.ku_ring.ui.chat.ui_model

import com.sendbird.android.SendbirdChat
import com.sendbird.android.message.AdminMessage
import com.sendbird.android.message.BaseMessage
import com.sendbird.android.message.UserMessage
import timber.log.Timber

fun List<BaseMessage>.toChatUiModelList(blackUserList: List<String>): List<ChatUiModel> {
    val currentUser = SendbirdChat.currentUser
    val chatUiModelList = mutableListOf<ChatUiModel>()

    if (currentUser == null) {
        Timber.e("currentUser is null!")
    }

    for (message in this) {
        if (message is AdminMessage) {
            chatUiModelList.add(message.toAdminMessageUiModel())
        } else if (message is UserMessage) {
            if (message.sender?.userId == currentUser?.userId) {
                chatUiModelList.add(message.toSentMessageUiModel(isPending = false))
            } else {
                if (!blackUserList.contains(message.sender?.userId)) {
                    chatUiModelList.add(message.toReceivedMessageUiModel())
                }
            }
        } else {
            Timber.e("message type is strange! ${message.messageId}")
        }
    }
    return chatUiModelList
}

fun UserMessage.toReceivedMessageUiModel(): ReceivedMessageUiModel {
    return ReceivedMessageUiModel(
        userId = this.sender?.userId ?: "",
        nickname = this.sender?.nickname ?: "",
        messageId = this.messageId,
        message = this.message,
        timeStamp = this.createdAt
    )
}

fun UserMessage.toSentMessageUiModel(isPending: Boolean?): SentMessageUiModel {
    return SentMessageUiModel(
        messageId = this.messageId,
        message = this.message,
        timeStamp = this.createdAt,
        requestId = this.requestId,
        isPending = isPending
    )
}

fun AdminMessage.toAdminMessageUiModel(): AdminMessageUiModel {
    return AdminMessageUiModel(
        messageId = this.messageId,
        message = this.message,
        timeStamp = this.createdAt
    )
}