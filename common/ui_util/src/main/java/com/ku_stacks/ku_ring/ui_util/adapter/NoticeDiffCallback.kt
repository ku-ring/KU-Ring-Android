package com.ku_stacks.ku_ring.ui_util.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ku_stacks.ku_ring.domain.Notice

object NoticeDiffCallback : DiffUtil.ItemCallback<Notice>() {
    override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
        return oldItem == newItem
    }
}