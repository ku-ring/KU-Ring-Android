package com.ku_stacks.ku_ring.ui.search.fragment_notice

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.websocket.response.SearchNoticeResponse
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding

class SearchNoticeViewHolder(
    private val binding: ItemNoticeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(notice: SearchNoticeResponse) {
        val transformedNotice = Notice(
            postedDate = notice.postedDate,
            subject = notice.subject,
            category = notice.category,
            url = "",
            articleId = notice.articleId,
            isNew = false,
            isRead = false,
            isSubscribing = false,
            tag = emptyList()
        )
        binding.noticeItem = transformedNotice
        binding.executePendingBindings()
    }
}