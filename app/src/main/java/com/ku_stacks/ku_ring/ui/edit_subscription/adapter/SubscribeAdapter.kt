package com.ku_stacks.ku_ring.ui.edit_subscription.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ItemSubscriptionBinding
import com.ku_stacks.ku_ring.ui.edit_subscription.viewholder.SubscribeViewHolder

class SubscribeAdapter(
    private val itemClick: (String) -> Unit,
) : ListAdapter<String, SubscribeViewHolder>(
    SubscriptionDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_subscription, parent, false)
        val binding = ItemSubscriptionBinding.bind(view)
        return SubscribeViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: SubscribeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object SubscriptionDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}