package com.ku_stacks.ku_ring.kuringbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.ai.repository.KuringBotRepository
import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class KuringBotViewModel @Inject constructor(
    private val kuringBotRepository: KuringBotRepository,
    private val messageCounter: KuringBotMessageCounter,
    preferences: PreferenceUtil,
) : ViewModel() {
    // TODO: 출시 때 토큰 수정하기
    private val token =
        "e1Xo4i7QTYiZhtqQv__QBz:APA91bE8rGtQ5IpIwn3MhC1cXBQ6wkpC-JeZzJ8-o8SclLhObCGphPQ-A5fgNHhUaqz12BN5fuqDuqjCY79cBvlQx1kEesPq7KZYSK-WmgAxQUu1kN04jVj3Zw0Aao2YY3z8jmy5Ezcj"
//        private val token = preferences.fcmToken

    private val _uiState = MutableStateFlow(KuringBotUIState.Empty)
    val uiState: StateFlow<KuringBotUIState>
        get() = _uiState.asStateFlow()

    private var currentKuringBotJob: Job? = null

    init {
        loadStoredMessages()
    }

    private fun loadStoredMessages() = viewModelScope.launch(Dispatchers.IO) {
        val messages = kuringBotRepository.getAllMessages()
        addInitialMessages(messages)
    }

    private fun addInitialMessages(messages: List<KuringBotMessage>) {
        val uiMessages = messageCounter.convertInitialUIMessages(messages)
        addUiMessages(uiMessages)
    }

    private fun addUiMessages(messages: List<KuringBotUIMessage>) {
        updateMessages(messages = uiState.value.messages + messages)
    }

    private fun updateMessages(messages: List<KuringBotUIMessage>) {
        _uiState.update { it.copy(messages = messages) }
    }

    fun onQuestionUpdate(question: String) {
        _uiState.update { it.copy(question = question.take(QUESTION_LENGTH_LIMIT)) }
    }

    fun sendQuestion() {
        val question = uiState.value.question
        stopKuringBotJob()
        clearQuestionUIState()
        setReceivingResponseState(true)

        currentKuringBotJob = startKuringBotJob(question)
    }

    private fun stopKuringBotJob() {
        currentKuringBotJob?.cancel()
        currentKuringBotJob = null

        setReceivingResponseState(false)
    }

    private fun clearQuestionUIState() {
        _uiState.update { it.copy(question = "") }
    }

    private fun setReceivingResponseState(value: Boolean) {
        _uiState.update { it.copy(isReceivingResponse = value) }
    }

    private fun startKuringBotJob(question: String) = viewModelScope.launch(Dispatchers.IO) {
        val questionMessage = KuringBotUIMessage.Question.create(question)
        addUiMessage(questionMessage)
        saveMessageToLocal(questionMessage)

        addLoadingToUIState()

        var response = KuringBotUIMessage.Response.Empty
        kuringBotRepository.openKuringBotSession(question, token) {
            response = response.copy(message = response.message + it)
            updateLastMessage(response)
        }
        addUiMessage(messageCounter.calculateQuestionsRemaining(response.postedTime))

        stopKuringBotJob(response)
    }

    private fun addUiMessage(message: KuringBotUIMessage) {
        addUiMessages(listOf(message))
        if (message is KuringBotUIMessage.Question) {
            messageCounter.increaseMessageCount(message.toDomain())
        }
    }

    private suspend fun saveMessageToLocal(message: KuringBotUIMessage.Savable) =
        withContext(Dispatchers.IO) {
            kuringBotRepository.insertMessage(message.toDomain())
        }

    private fun addLoadingToUIState() {
        addUiMessage(KuringBotUIMessage.Loading)
    }

    private fun updateLastMessage(message: KuringBotUIMessage) {
        val newMessages = uiState.value.messages.dropLast(1) + message
        updateMessages(newMessages)
    }

    private suspend fun stopKuringBotJob(response: KuringBotUIMessage.Response) {
        saveMessageToLocal(response)
        stopKuringBotJob()
    }

    fun stopReceivingResponse() {
        if (currentKuringBotJob == null) return

        viewModelScope.launch {
            val lastMessage = uiState.value.messages.lastOrNull()

            if (lastMessage is KuringBotUIMessage.Loading) {
                updateMessages(uiState.value.messages.dropLast(1))
            }

            uiState.value.messages.lastOrNull { it is KuringBotUIMessage.Savable }?.let {
                val savable = it as KuringBotUIMessage.Savable
                if (savable is KuringBotUIMessage.Response) {
                    saveMessageToLocal(savable)
                }
                addUiMessage(messageCounter.calculateQuestionsRemaining(savable.postedTime))
            }

            stopKuringBotJob()
        }
    }

    fun setSendQuestionDialogVisibility(value: Boolean) {
        _uiState.update { it.copy(isSendQuestionDialogVisible = value) }
    }

    companion object {
        // TODO: 출시 때 2로 수정하기
        private const val QUESTION_LENGTH_LIMIT = 200
    }

}