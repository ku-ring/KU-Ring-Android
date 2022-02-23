package com.ku_stacks.ku_ring.ui.my_notification.viewholder

import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.databinding.ItemDateBinding
import com.ku_stacks.ku_ring.ui.my_notification.NotificationAdapter

class DateViewHolder(
    private val binding: ItemDateBinding
) : NotificationAdapter.ViewHolder(binding.root) {

    fun bind(pushInfo: Push, isNewDay: Boolean) {
        binding.notificationItem = pushInfo
        binding.executePendingBindings()
    }
}