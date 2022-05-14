package com.ku_stacks.ku_ring.ui.chat.viewholder

import com.ku_stacks.ku_ring.adapter.visibleIf
import com.ku_stacks.ku_ring.databinding.ItemChatReceiveBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.ReceivedMessageUiModel

class ReceiveViewHolder(
    private val binding: ItemChatReceiveBinding
) : SealedChatViewHolder(binding.root) {

    fun bind(message: ReceivedMessageUiModel, showDate: Boolean) {
        binding.receivedMessageUiModel = message
        binding.chatDateTv.visibleIf(showDate)
    }
}