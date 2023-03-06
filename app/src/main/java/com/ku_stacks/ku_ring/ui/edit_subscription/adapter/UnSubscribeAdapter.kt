package com.ku_stacks.ku_ring.ui.edit_subscription.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ItemUnsubscriptionBinding
import com.ku_stacks.ku_ring.ui.edit_subscription.viewholder.UnSubscribeViewHolder

class UnSubscribeAdapter(
    private val itemClick: (String) -> Unit,
) : ListAdapter<String, UnSubscribeViewHolder>(
    UnSubscriptionDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnSubscribeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_unsubscription, parent, false)
        val binding = ItemUnsubscriptionBinding.bind(view)
        return UnSubscribeViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: UnSubscribeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object UnSubscriptionDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}