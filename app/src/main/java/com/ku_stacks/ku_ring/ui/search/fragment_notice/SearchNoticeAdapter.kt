package com.ku_stacks.ku_ring.ui.search.fragment_notice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.websocket.response.SearchNoticeResponse
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding

class SearchNoticeAdapter(
    private val itemClick: (Notice) -> Unit
) : ListAdapter<SearchNoticeResponse, SearchNoticeViewHolder>(SearchNoticeDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchNoticeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notice, parent, false)
        val binding = ItemNoticeBinding.bind(view)
        return SearchNoticeViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: SearchNoticeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object SearchNoticeDiffCallback : DiffUtil.ItemCallback<SearchNoticeResponse>() {
        override fun areItemsTheSame(oldItem: SearchNoticeResponse, newItem: SearchNoticeResponse): Boolean {
            return oldItem.articleId == newItem.articleId
        }

        override fun areContentsTheSame(oldItem: SearchNoticeResponse, newItem: SearchNoticeResponse): Boolean {
            return oldItem.articleId == newItem.articleId
        }
    }
}