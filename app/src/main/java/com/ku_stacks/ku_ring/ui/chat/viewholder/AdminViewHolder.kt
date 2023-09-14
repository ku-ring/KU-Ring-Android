package com.ku_stacks.ku_ring.ui.chat.viewholder

import com.ku_stacks.ku_ring.databinding.ItemChatAdminBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.AdminMessageUiModel
import com.ku_stacks.ku_ring.ui_util.visibleIf

class AdminViewHolder(
    private val binding: ItemChatAdminBinding
) : SealedChatViewHolder(binding.root) {

    fun bind(message: AdminMessageUiModel, showDate: Boolean) {
        binding.adminMessageUiModel = message
        binding.chatDateTv.visibleIf(showDate)
    }
}