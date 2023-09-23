package com.ku_stacks.ku_ring.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui.chat.ui_model.ChatUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.ReceivedMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.SentMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.toAdminMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.toChatUiModelList
import com.ku_stacks.ku_ring.ui.chat.ui_model.toReceivedMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.toSentMessageUiModel
import com.ku_stacks.ku_ring.ui_util.SingleLiveEvent
import com.ku_stacks.ku_ring.user.api.FeedbackClient
import com.ku_stacks.ku_ring.user.api.request.FeedbackRequest
import com.ku_stacks.ku_ring.user.repository.UserRepository
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
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val pref: PreferenceUtil,
    private val feedbackClient: FeedbackClient,
    private val repository: UserRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val _toastEvent = SingleLiveEvent<Int>()
    val toastEvent: LiveData<Int>
        get() = _toastEvent

    private val normalMessageList = mutableListOf<ChatUiModel>()
    private val pendingMessageList = mutableListOf<SentMessageUiModel>()
    private val _chatUiModelList = MutableLiveData<List<ChatUiModel>>()
    val chatUiModelList: LiveData<List<ChatUiModel>>
        get() = _chatUiModelList

    private lateinit var blackUserList: List<String>

    private val _hasPrevious = MutableLiveData<Boolean>()
    val hasPrevious: LiveData<Boolean>
        get() = _hasPrevious

    val isLoading = MutableLiveData(false)

    private val _readyToBottomScrollEvent = SingleLiveEvent<Unit>()
    val readyToBottomScrollEvent: LiveData<Unit>
        get() = _readyToBottomScrollEvent

    private var kuringChannel: OpenChannel? = null

    init {
        updateBlackUserList()
        enterChannel()
        addSendbirdHandler()
    }

    private fun updateBlackUserList() {
        disposable.add(
            repository.getBlackUserList()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    blackUserList = it
                }, {
                    Timber.e("getBlackUserList error $it")
                })
        )
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

    fun deletePendingMessage(sentMessageUiModel: SentMessageUiModel) {
        pendingMessageList.removeIf { it.requestId == sentMessageUiModel.requestId }
        _chatUiModelList.postValue(normalMessageList + pendingMessageList)
    }

    private fun addPendingMessage(message: UserMessage?) {
        message?.let {
            pendingMessageList.add(it.toSentMessageUiModel(isPending = true))
            _chatUiModelList.postValue(normalMessageList + pendingMessageList)
        }
    }

    private fun updateErrorMessage(message: UserMessage?) {
        message?.let { msg ->
            pendingMessageList.forEachIndexed { index, pendingMessage ->
                if (pendingMessage.requestId == msg.requestId) {
                    pendingMessageList[index] = msg.toSentMessageUiModel(null)
                    return@forEachIndexed
                }
            }
            _chatUiModelList.postValue(normalMessageList + pendingMessageList)
        }
    }

    private fun updateSucceedMessage(message: UserMessage?) {
        message?.let { msg ->
            pendingMessageList.removeIf { it.requestId == msg.requestId }
            normalMessageList.add(msg.toSentMessageUiModel(isPending = false))
            _chatUiModelList.postValue(normalMessageList + pendingMessageList)
        }
    }

    fun fetchPreviousMessageList(timeStamp: Long) {
        isLoading.value = true

        val params = MessageListParams().apply {
            previousResultSize = 100
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
                _hasPrevious.value = false
                isLoading.postValue(false)
            } else {
                _hasPrevious.value = messageList.size >= params.previousResultSize
                normalMessageList.addAll(0, messageList.toChatUiModelList(blackUserList))
                _chatUiModelList.postValue(normalMessageList + pendingMessageList)
            }
        }
    }

    fun fetchLatestMessageList(timeStamp: Long) {
        isLoading.value = true

        val params = MessageListParams().apply {
            nextResultSize = 100
        }
        kuringChannel?.getMessagesByTimestamp(timeStamp, params) { messageList, e ->
            if (e != null) {
                isLoading.postValue(false)
                Timber.e("fetchLatestMessageList error [${e.code}] : ${e.message}")
                return@getMessagesByTimestamp
            }

            if (messageList.isNullOrEmpty()) {
                isLoading.postValue(false)
                return@getMessagesByTimestamp
            }

            normalMessageList.addAll(messageList.toChatUiModelList(blackUserList))
            _chatUiModelList.postValue(normalMessageList + pendingMessageList)
            val hasNext = messageList.size >= params.nextResultSize
            if (hasNext) {
                fetchLatestMessageList(messageList.last().createdAt)
            }
        }
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

                    val lastMessageTime = normalMessageList.lastOrNull()?.timeStamp
                    lastMessageTime?.let { timeStamp ->
                        fetchLatestMessageList(timeStamp)
                    }
                }
            })

        SendbirdChat.addChannelHandler(
            CHANNEL_HANDLER_ID,
            object : OpenChannelHandler() {
                override fun onMessageReceived(channel: BaseChannel, message: BaseMessage) {
                    Timber.e("onMessageReceived")
                    when (message) {
                        is UserMessage -> normalMessageList.add(message.toReceivedMessageUiModel())
                        is AdminMessage -> normalMessageList.add(message.toAdminMessageUiModel())
                    }
                    _chatUiModelList.postValue(normalMessageList + pendingMessageList)
                }
            }
        )
    }

    fun reportMessage(messageUiModel: ReceivedMessageUiModel) {
        val reporter = SendbirdChat.currentUser?.userId ?: ""
        val feedbackContent =
            "🤬 $reporter 가 ${messageUiModel.userId} 를 신고했습니다 - 메세지 내용 : ${messageUiModel.message}"

        // send to Kuring server
        disposable.add(
            feedbackClient.sendFeedback(
                token = pref.fcmToken ?: "",
                feedbackRequest = FeedbackRequest(content = feedbackContent),
            )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.isSuccess) {
                        _toastEvent.postValue(R.string.report_success)
                    } else {
                        _toastEvent.postValue(R.string.report_fail)
                    }
                }, {
                    _toastEvent.postValue(R.string.report_error)
                })
        )

        // send to sendbird server
        reportToSendbirdServer(messageUiModel)
    }

    private fun reportToSendbirdServer(messageUiModel: ReceivedMessageUiModel) {
        val params = MessageListParams().apply {
            isInclusive = true
            previousResultSize = 0
            nextResultSize = 0
        }
        kuringChannel?.getMessagesByMessageId(messageUiModel.messageId, params) { messages, e1 ->
            if (e1 != null) {
                Timber.e("getMessagesByMessageId error [${e1.code}] ${e1.message}")
                return@getMessagesByMessageId
            }
            if (messages?.size == 1) {
                kuringChannel?.reportMessage(
                    message = messages[0],
                    reportCategory = BaseChannel.ReportCategory.INAPPROPRIATE,
                    reportDescription = "",
                ) { e2 ->
                    if (e2 != null) {
                        Timber.e("reportToSendbirdServer error [${e2.code}] : ${e2.message}")
                        return@reportMessage
                    }
                    Timber.e("reportToSendbirdServer success")
                }
            }
        }
    }

    fun blockUser(messageUiModel: ReceivedMessageUiModel) {
        val userId = messageUiModel.userId
        val nickname = messageUiModel.nickname

        disposable.add(
            repository.blockUser(userId, nickname)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.e("blockUser success")
                    _toastEvent.postValue(R.string.block_success)
                    updateBlackUserList()
                }, {
                    Timber.e("blockUser error $it")
                    _toastEvent.postValue(R.string.block_fail)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        SendbirdChat.removeConnectionHandler(CONNECTION_HANDLER_ID)
        SendbirdChat.removeChannelHandler(CHANNEL_HANDLER_ID)

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    companion object {
        const val CONNECTION_HANDLER_ID = "KURING_CONNECTION_ID"
        const val CHANNEL_HANDLER_ID = "KURING_CHANNEL_ID"
    }
}