package com.ku_stacks.ku_ring.ui.notice_storage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ItemSavedNoticeBinding
import com.ku_stacks.ku_ring.ui.notice_storage.ui_model.SavedNoticeUiModel

class NoticeStorageAdapter(
    private val onItemClick: (SavedNoticeUiModel) -> Unit
) : ListAdapter<SavedNoticeUiModel, NoticeStorageAdapter.ViewHolder>(
    SavedNoticeDiffCallback()
) {
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_notice, parent, false)
        val binding = ItemSavedNoticeBinding.bind(view)
        return SavedNoticeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as SavedNoticeViewHolder).bind(item)
    }
}