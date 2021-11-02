package com.ku_stacks.ku_ring.ui.my_notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.entity.Push
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding
import com.ku_stacks.ku_ring.ui.my_notification.viewholder.NotificationViewHolder

class NotificationAdapter(
    private val itemClick: (Push) -> Unit,
    private val onBindItem: (Push) -> Unit
): ListAdapter<Push, NotificationViewHolder>(
    NotificationDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        val binding = ItemNotificationBinding.bind(view)
        return NotificationViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            onBindItem(it)
        }
    }

    object NotificationDiffCallback : DiffUtil.ItemCallback<Push>() {
        override fun areItemsTheSame(oldItem: Push, newItem: Push): Boolean {
            return oldItem.articleId == newItem.articleId
        }

        override fun areContentsTheSame(oldItem: Push, newItem: Push): Boolean {
            return oldItem.articleId == newItem.articleId
                    && oldItem.isNewDay == newItem.isNewDay
        }
    }
}