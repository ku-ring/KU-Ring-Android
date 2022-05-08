package com.ku_stacks.ku_ring.ui.chat.viewholder

import com.ku_stacks.ku_ring.databinding.ItemChatAdminBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.AdminMessageUiModel

class AdminViewHolder(
    private val binding: ItemChatAdminBinding
) : ChatViewHolder(binding.root) {

    fun bind(message: AdminMessageUiModel) {
        binding.adminMessageUiModel = message
    }
}