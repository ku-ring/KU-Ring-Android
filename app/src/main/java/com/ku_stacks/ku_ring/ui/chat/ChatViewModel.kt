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

    private val _chatUiModelList = mutableListOf<ChatUiModel>()
    val chatUiModelList = MutableLiveData<List<ChatUiModel>>()

    val hasPrevious = MutableLiveData<Boolean>()
    val hasNext = MutableLiveData<Boolean>()

    val isLoading = MutableLiveData(false)

    private val _scrollToBottomEvent = SingleLiveEvent<Unit>()
    val scrollToBottomEvent: LiveData<Unit>
        get() = _scrollToBottomEvent

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
            Timber.e("Sendbird sendMessage success")
            updateSucceedMessage(message)
        }
        addPendingMessage(pendingMessage)
        // TODO : _scrollToBottomEvent.call() //addPendingMessage의 postValue가 비동기라 여기서 call 하는건 의미가 없다.
    }

    private fun addPendingMessage(message: UserMessage?) {
        message?.let {
            _chatUiModelList.add(it.toSentMessageUiModel(isPending = true))
            chatUiModelList.postValue(_chatUiModelList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateErrorMessage(message: UserMessage?) {
        message?.let { msg ->
            _chatUiModelList.forEachIndexed { index, chatUiModel ->
                if (chatUiModel is SentMessageUiModel && chatUiModel.requestId == msg.requestId) {
                    _chatUiModelList[index] = msg.toSentMessageUiModel(null)
                    return@forEachIndexed
                }
            }
            chatUiModelList.postValue(_chatUiModelList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateSucceedMessage(message: UserMessage?) {
        message?.let { msg ->
            _chatUiModelList.removeIf { (it is SentMessageUiModel) && it.requestId == msg.requestId }
            _chatUiModelList.add(msg.toSentMessageUiModel(isPending = false))
            chatUiModelList.postValue(_chatUiModelList)
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
                _chatUiModelList.addAll(0, messageList.toChatUiModelList())
                chatUiModelList.postValue(_chatUiModelList)
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
                        is UserMessage -> _chatUiModelList.add(message.toReceivedMessageUiModel())
                        is AdminMessage -> _chatUiModelList.add(message.toAdminMessageUiModel())
                    }
                    chatUiModelList.postValue(_chatUiModelList)
                }

                override fun onMessageDeleted(channel: BaseChannel, msgId: Long) {
                    //TODO
                }

                override fun onMessageUpdated(channel: BaseChannel, message: BaseMessage) {
                    //TODO
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