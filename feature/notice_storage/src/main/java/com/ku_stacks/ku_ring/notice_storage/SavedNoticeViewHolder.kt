package com.ku_stacks.ku_ring.notice_storage

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice_storage.databinding.ItemSavedNoticeBinding

class SavedNoticeViewHolder(
    private val binding: ItemSavedNoticeBinding,
    private val onClick: (Notice) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.mainLayout.setOnClickListener {
            binding.noticeItem?.let(onClick)
        }
    }

    fun bind(notice: Notice) {
        setupTag(notice.tag)
        binding.apply {
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