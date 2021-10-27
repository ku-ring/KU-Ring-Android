package com.ku_stacks.ku_ring.ui.my_notification

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
    private val itemClick: (PushEntity) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pushInfo: PushEntity) {
        binding.notificationItem = pushInfo
        binding.root.setOnClickListener {
            itemClick(pushInfo)
        }
        binding.executePendingBindings()
    }
}