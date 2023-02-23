package com.ku_stacks.ku_ring.ui.main.notice.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding

class NoticePagingAdapter(
    private val itemClick: (Notice) -> Unit,
    private val onBindItem: (Notice) -> Unit,
) : PagingDataAdapter<Notice, NoticeViewHolder>(NoticeDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notice, parent, false)
        val binding = ItemNoticeBinding.bind(view)
        return NoticeViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            onBindItem(it)
        }
    }

    object NoticeDiffCallback : DiffUtil.ItemCallback<Notice>() {
        override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
            return oldItem == newItem
        }
    }
}