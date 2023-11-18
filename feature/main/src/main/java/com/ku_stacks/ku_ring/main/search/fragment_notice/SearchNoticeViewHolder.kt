package com.ku_stacks.ku_ring.main.search.fragment_notice

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.databinding.ItemNoticeBinding

class SearchNoticeViewHolder(
    private val binding: ItemNoticeBinding,
    private val itemClick: (Notice) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(notice: Notice) {
        binding.noticeItem = notice
        binding.root.setOnClickListener {
            itemClick(notice)
        }
        binding.executePendingBindings()
    }
}