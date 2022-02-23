package com.ku_stacks.ku_ring.ui.my_notification.viewholder

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding
import com.ku_stacks.ku_ring.ui.my_notification.NotificationAdapter

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
    private val itemClick: (Push) -> (Unit)
) : NotificationAdapter.ViewHolder(binding.root) {

    fun bind(pushInfo: Push) {
        binding.notificationItem = pushInfo
        setupTag(pushInfo.tag)
        binding.notificationMainLayout.setOnClickListener {
            itemClick(pushInfo)
        }
        binding.executePendingBindings()
    }

    private fun setupTag(tagList: List<String>) {
        val colors: MutableList<IntArray> = arrayListOf()
        val color = intArrayOf(
            ContextCompat.getColor(binding.root.context, R.color.kus_secondary_gray), //tag background color
            Color.TRANSPARENT, //tag border color
            Color.WHITE, //tag text color
            Color.TRANSPARENT) //tag selected background color

        for (item in tagList) {
            colors.add(color)
        }
        binding.notificationTagContainer.setTags(tagList, colors)
    }
}