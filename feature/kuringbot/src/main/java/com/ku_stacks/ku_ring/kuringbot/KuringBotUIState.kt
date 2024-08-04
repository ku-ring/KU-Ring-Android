package com.ku_stacks.ku_ring.kuringbot

data class KuringBotUIState(
    val question: String,
    val messages: List<KuringBotUIMessage>,
    val isSendQuestionDialogVisible: Boolean,
    val isReceivingResponse: Boolean,
) {
    companion object {
        val Empty = KuringBotUIState(
            question = "",
            messages = emptyList(),
            isSendQuestionDialogVisible = false,
            isReceivingResponse = false,
        )
    }
}