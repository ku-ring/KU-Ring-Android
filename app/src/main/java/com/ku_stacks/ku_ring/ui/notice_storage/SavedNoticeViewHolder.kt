package com.ku_stacks.ku_ring.ui.notice_storage

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ItemSavedNoticeBinding
import com.ku_stacks.ku_ring.ui.notice_storage.ui_model.SavedNoticeUiModel

class SavedNoticeViewHolder(
    private val binding: ItemSavedNoticeBinding,
    private val onClick: (SavedNoticeUiModel) -> Unit
) : NoticeStorageAdapter.ViewHolder(binding.root) {
    fun bind(savedNoticeUiModel: SavedNoticeUiModel) {
        setupTag(savedNoticeUiModel.tag)
        binding.apply {
            mainLayout.setOnClickListener { onClick(savedNoticeUiModel) }
            savedNotificationUiModel = savedNoticeUiModel
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