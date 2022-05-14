package com.ku_stacks.ku_ring.ui.chat.viewholder

import com.ku_stacks.ku_ring.adapter.visibleIf
import com.ku_stacks.ku_ring.databinding.ItemChatAdminBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.AdminMessageUiModel

class AdminViewHolder(
    private val binding: ItemChatAdminBinding
) : SealedChatViewHolder(binding.root) {

    fun bind(message: AdminMessageUiModel, showDate: Boolean) {
        binding.adminMessageUiModel = message
        binding.chatDateTv.visibleIf(showDate)
    }
}