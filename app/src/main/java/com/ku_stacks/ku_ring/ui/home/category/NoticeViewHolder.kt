package com.ku_stacks.ku_ring.ui.home.category

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.adapter.BindingAdapters.backgroundGrayIf
import com.ku_stacks.ku_ring.adapter.BindingAdapters.visibleIf
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding

class NoticeViewHolder(
    private val binding: ItemNoticeBinding,
    private val itemClick: (Notice) -> (Unit),
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(notice: Notice) {
        binding.noticeItem = notice
        binding.root.setOnClickListener {
            binding.noticeIsReadLayout.backgroundGrayIf(true)
            binding.noticeIsNewPoint.visibleIf(false)
            binding.executePendingBindings()
            itemClick(notice)
        }
        binding.executePendingBindings()
    }
}