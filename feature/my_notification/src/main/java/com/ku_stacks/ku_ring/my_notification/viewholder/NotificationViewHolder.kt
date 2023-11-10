package com.ku_stacks.ku_ring.my_notification.viewholder

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ku_stacks.ku_ring.my_notification.NotificationAdapter
import com.ku_stacks.ku_ring.my_notification.R
import com.ku_stacks.ku_ring.my_notification.databinding.ItemNotificationBinding
import com.ku_stacks.ku_ring.my_notification.ui_model.PushContentUiModel

class NotificationViewHolder(
    private val binding: ItemNotificationBinding,
    private val itemClick: (PushContentUiModel) -> (Unit)
) : NotificationAdapter.ViewHolder(binding.root) {

    fun bind(pushContent: PushContentUiModel) {
        binding.pushContentUiModel = pushContent
        setupTag(pushContent.tag)
        binding.notificationMainLayout.setOnClickListener {
            itemClick(pushContent)
        }
        binding.executePendingBindings()
    }

    private fun setupTag(tagList: List<String>) {
        val colors: MutableList<IntArray> = arrayListOf()
        val color = intArrayOf(
            ContextCompat.getColor(
                binding.root.context,
                R.color.kus_secondary_gray
            ), //tag background color
            Color.TRANSPARENT, //tag border color
            Color.WHITE, //tag text color
            Color.TRANSPARENT
        ) //tag selected background color

        for (item in tagList) {
            colors.add(color)
        }
        binding.notificationTagContainer.setTags(tagList, colors)
    }
}