package com.ku_stacks.ku_ring.ui.setting_notification.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.databinding.ItemSubscriptionBinding

class SubscribeViewHolder(
    private val binding: ItemSubscriptionBinding,
    private val itemClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: String) {
        binding.category = category
        binding.subscriptionCategoryBt.setOnClickListener {
            itemClick(category)
        }
        binding.executePendingBindings()
    }
}