package com.ku_stacks.ku_ring.ui.notice_storage

import androidx.recyclerview.widget.DiffUtil
import com.ku_stacks.ku_ring.ui.notice_storage.ui_model.SavedNoticeUiModel

class SavedNoticeDiffCallback : DiffUtil.ItemCallback<SavedNoticeUiModel>() {
    override fun areItemsTheSame(
        oldItem: SavedNoticeUiModel,
        newItem: SavedNoticeUiModel
    ): Boolean {
        return oldItem.articleId == newItem.articleId
    }

    override fun areContentsTheSame(
        oldItem: SavedNoticeUiModel,
        newItem: SavedNoticeUiModel
    ): Boolean {
        return oldItem == newItem
    }
}