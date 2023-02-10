package com.ku_stacks.ku_ring.ui.notice_storage

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.ItemSavedNoticeBinding

class SavedNoticeViewHolder(
    private val binding: ItemSavedNoticeBinding,
    private val onClick: (Notice) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(notice: Notice) {
        setupTag(notice.tag)
        binding.apply {
            mainLayout.setOnClickListener { onClick(notice) }
            noticeItem = notice
            executePendingBindings()
        }
    }

    private fun setupTag(tags: List<String>) {
        val colors = tags.map {
            // See NotificationViewHolder.setupTag()
            intArrayOf(
                ContextCompat.getColor(binding.root.context, R.color.kus_secondary_gray),
                Color.TRANSPARENT,
                Color.WHITE,
                Color.TRANSPARENT,
            )
        }
        binding.noticeTagContainer.setTags(tags, colors)
    }
}