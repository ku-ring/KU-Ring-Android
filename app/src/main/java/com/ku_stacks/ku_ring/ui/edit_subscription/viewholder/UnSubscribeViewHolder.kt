package com.ku_stacks.ku_ring.ui.edit_subscription.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.databinding.ItemUnsubscriptionBinding

class UnSubscribeViewHolder(
    private val binding: ItemUnsubscriptionBinding,
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