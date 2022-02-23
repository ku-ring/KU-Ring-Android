package com.ku_stacks.ku_ring.ui.my_notification.viewholder

import com.ku_stacks.ku_ring.databinding.ItemDateBinding
import com.ku_stacks.ku_ring.ui.my_notification.NotificationAdapter
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDateHeaderUiModel

class DateViewHolder(
    private val binding: ItemDateBinding
) : NotificationAdapter.ViewHolder(binding.root) {

    fun bind(pushDate: PushDateHeaderUiModel) {
        binding.pushDateHeaderUiModel = pushDate
        binding.executePendingBindings()
    }
}