package com.ku_stacks.ku_ring.ui.home.category

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding
import timber.log.Timber

class NoticeViewHolder(private val binding: ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(notice: Notice) {
        Timber.e("received notice : $notice")
        binding.noticeItem = notice
        binding.executePendingBindings()
    }
}