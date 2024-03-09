package com.ku_stacks.ku_ring.notice_storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice_storage.databinding.ItemSavedNoticeBinding
import com.ku_stacks.ku_ring.ui_util.adapter.NoticeDiffCallback

@Deprecated("Compose migration 이후 삭제할 예정")
class NoticeStorageAdapter(
    private val onItemClick: (Notice) -> Unit
) : ListAdapter<Notice, ViewHolder>(NoticeDiffCallback) {

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