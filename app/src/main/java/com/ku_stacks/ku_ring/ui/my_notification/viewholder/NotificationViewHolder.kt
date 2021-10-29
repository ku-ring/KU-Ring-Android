package com.ku_stacks.ku_ring.ui.my_notification.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.entity.Push
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
    private val itemClick: (Push) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pushInfo: Push) {
        binding.notificationItem = pushInfo
        binding.notificationMainLayout.setOnClickListener {
            itemClick(pushInfo)
        }
        binding.executePendingBindings()
    }
}