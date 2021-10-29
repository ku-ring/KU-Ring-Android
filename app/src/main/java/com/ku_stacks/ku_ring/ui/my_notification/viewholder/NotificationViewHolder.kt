package com.ku_stacks.ku_ring.ui.my_notification.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
    private val itemClick: (PushEntity) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pushInfo: PushEntity, isNewDay: Boolean) {
        binding.notificationItem = pushInfo
        binding.notificationMainLayout.setOnClickListener {
            itemClick(pushInfo)
        }
        binding.notificationDateTxt.visibility = when (isNewDay) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        binding.executePendingBindings()
    }
}