package com.ku_stacks.ku_ring.ui.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import com.ku_stacks.ku_ring.ui.chat.ui_model.*
import com.sendbird.android.SendbirdChat
import com.sendbird.android.channel.BaseChannel
import com.sendbird.android.channel.OpenChannel
import com.sendbird.android.handler.ConnectionHandler
import com.sendbird.android.handler.OpenChannelHandler
import com.sendbird.android.message.AdminMessage
import com.sendbird.android.message.BaseMessage
import com.sendbird.android.message.UserMessage
import com.sendbird.android.params.MessageListParams
import com.sendbird.android.params.UserMessageCreateParams
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(

) : ViewModel() {

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val uiModelList = mutableListOf<ChatUiModel>()
    private val pendingMessageList = mutableListOf<SentMessageUiModel>()
    private val _chatUiModelList = MutableLiveData<List<ChatUiModel>>()
    val chatUiModelList: LiveData<List<ChatUiModel>>
        get() = _chatUiModelList

    val hasPrevious = MutableLiveData<Boolean>()
    val hasNext = MutableLiveData<Boolean>()

    val isLoading = MutableLiveData(false)

    private val _readyToBottomScrollEvent = SingleLiveEvent<Unit>()
    val readyToBottomScrollEvent: LiveData<Unit>
        get() = _readyToBottomScrollEvent

    private var kuringChannel: OpenChannel? = null

    init {
        enterChannel()
        addSendbirdHandler()
    }

    private fun enterChannel() {
        val channelUrl = BuildConfig.KURING_CAMPUS_OPEN_CHANNEL_URL
        OpenChannel.getChannel(channelUrl) { channel, e1 ->
            if (e1 != null) {
                Timber.e("Sendbird getChannel error [${e1.code}] : ${e1.message}")
                _dialogEvent.postValue(R.string.chat_get_channel_error)
                return@getChannel
            }
            channel?.enter { e2 ->
                if (e2 != null) {
                    Timber.e("Sendbird enterChannel error [${e2.code}] : ${e2.message}")
                    _dialogEvent.postValue(R.string.chat_enter_channel_error)
                    return@enter
                }
                kuringChannel = channel
                Timber.e("Sendbird openChannel enter success")

                fetchPreviousMessageList(Long.MAX_VALUE)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendMessage(text: String) {
        if (text.isEmpty()) {
            return
        }
        val params = UserMessageCreateParams(text)
        val pendingMessage = kuringChannel?.sendUserMessage(params) { message, e ->
            if (e != null) {
                Timber.e("Sendbird sendMessage error [${e.code}] : ${e.message}")
                updateErrorMessage(message)
                return@sendUserMessage
            }
            updateSucceedMessage(message)
        }
        _readyToBottomScrollEvent.call()
        addPendingMessage(pendingMessage)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePendingMessage(sentMessageUiModel: SentMessageUiModel) {
        pendingMessageList.removeIf { it.requestId == sentMessageUiModel.requestId }
        _chatUiModelList.postValue(uiModelList + pendingMessageList)
    }

    private fun addPendingMessage(message: UserMessage?) {
        message?.let {
            pendingMessageList.add(it.toSentMessageUiModel(isPending = true))
            _chatUiModelList.postValue(uiModelList + pendingMessageList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateErrorMessage(message: UserMessage?) {
        message?.let { msg ->
            pendingMessageList.forEachIndexed { index, pendingMessage ->
                if (pendingMessage.requestId == msg.requestId) {
                    pendingMessageList[index] = msg.toSentMessageUiModel(null)
                    return@forEachIndexed
                }
            }
            _chatUiModelList.postValue(uiModelList + pendingMessageList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateSucceedMessage(message: UserMessage?) {
        message?.let { msg ->
            pendingMessageList.removeIf { it.requestId == msg.requestId }
            uiModelList.add(msg.toSentMessageUiModel(isPending = false))
            _chatUiModelList.postValue(uiModelList + pendingMessageList)
        }
    }

    fun fetchPreviousMessageList(timeStamp: Long) {
        isLoading.value = true

        val params = MessageListParams().apply {
            previousResultSize = 20
        }
        kuringChannel?.getMessagesByTimestamp(timeStamp, params) { messageList, e ->
            if (e != null) {
                isLoading.postValue(false)
                Timber.e("fetchPreviousMessageList error [${e.code}] : ${e.message}")
                return@getMessagesByTimestamp
            }
            if (messageList == null) {
                isLoading.postValue(false)
                Timber.e("fetchPreviousMessageList is null")
                return@getMessagesByTimestamp
            }

            if (messageList.isEmpty()) {
                hasPrevious.value = false
            } else {
                hasPrevious.value = messageList.size >= params.previousResultSize
                uiModelList.addAll(0, messageList.toChatUiModelList())
                _chatUiModelList.postValue(uiModelList + pendingMessageList)
            }
        }
    }

    fun fetchLatestMessageList(timeStamp: Long) {
        // TODO
    }

    private fun addSendbirdHandler() {
        SendbirdChat.addConnectionHandler(
            CONNECTION_HANDLER_ID,
            object : ConnectionHandler {
                override fun onConnected(userId: String) {
                    Timber.e("Sendbird connection connected")
                }

                override fun onDisconnected(userId: String) {
                    Timber.e("Sendbird connection disconnected")
                }

                override fun onReconnectFailed() {
                    Timber.e("Sendbird reconnection failed")
                }

                override fun onReconnectStarted() {
                    Timber.e("Sendbird reconnection started")
                }

                override fun onReconnectSucceeded() {
                    Timber.e("Sendbird reconnection succeed")
                    // TODO : 연결이 끊어진 사이의 메세지들에 대한 load 해야함
                }
            })

        SendbirdChat.addChannelHandler(
            CHANNEL_HANDLER_ID,
            object : OpenChannelHandler() {
                override fun onMessageReceived(channel: BaseChannel, message: BaseMessage) {
                    Timber.e("onMessageReceived")
                    when (message) {
                        is UserMessage -> uiModelList.add(message.toReceivedMessageUiModel())
                        is AdminMessage -> uiModelList.add(message.toAdminMessageUiModel())
                    }
                    _chatUiModelList.postValue(uiModelList + pendingMessageList)
                }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        SendbirdChat.removeConnectionHandler(CONNECTION_HANDLER_ID)
        SendbirdChat.removeChannelHandler(CHANNEL_HANDLER_ID)
    }

    companion object {
        const val CONNECTION_HANDLER_ID = "KURING_CONNECTION_ID"
        const val CHANNEL_HANDLER_ID = "KURING_CHANNEL_ID"
    }
}