package com.ku_stacks.ku_ring.ui.my_notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val itemClick: (PushEntity) -> Unit,
    private val onBindItem: (PushEntity) -> Unit
): ListAdapter<PushEntity, NotificationViewHolder>(
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

    object NotificationDiffCallback : DiffUtil.ItemCallback<PushEntity>() {
        override fun areItemsTheSame(oldItem: PushEntity, newItem: PushEntity): Boolean {
            return oldItem.articleId == newItem.articleId
        }

        override fun areContentsTheSame(oldItem: PushEntity, newItem: PushEntity): Boolean {
            return oldItem.articleId == newItem.articleId
        }
    }
}