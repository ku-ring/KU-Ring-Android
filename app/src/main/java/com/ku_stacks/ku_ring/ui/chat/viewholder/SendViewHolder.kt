package com.ku_stacks.ku_ring.ui.chat.viewholder

import com.ku_stacks.ku_ring.databinding.ItemChatSendBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.SentMessageUiModel

class SendViewHolder(
    private val binding: ItemChatSendBinding
) : SealedChatViewHolder(binding.root) {

    fun bind(message: SentMessageUiModel) {
        binding.sentMessageUiModel = message
    }
}