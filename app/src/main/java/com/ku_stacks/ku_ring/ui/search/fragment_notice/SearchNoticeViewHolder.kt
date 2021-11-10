package com.ku_stacks.ku_ring.ui.search.fragment_notice

import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.websocket.response.SearchNoticeResponse
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding

class SearchNoticeViewHolder(
    private val binding: ItemNoticeBinding,
    private val itemClick: (Notice) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(notice: SearchNoticeResponse) {
        val url = generateUrl(notice.articleId, notice.category, notice.baseUrl)

        val transformedNotice = Notice(
            postedDate = notice.postedDate,
            subject = notice.subject,
            category = notice.category,
            url = url,
            articleId = notice.articleId,
            isNew = false,
            isRead = false,
            isSubscribing = false,
            tag = emptyList()
        )
        binding.noticeItem = transformedNotice
        binding.root.setOnClickListener {
            itemClick(transformedNotice)
        }
        binding.executePendingBindings()
    }

    private fun generateUrl(articleId: String, category: String, baseUrl: String): String {
        return if (category == "library") {
            "$baseUrl/$articleId"
        } else {
            "$baseUrl?id=$articleId"
        }
    }
}