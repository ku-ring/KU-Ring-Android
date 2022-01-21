package com.ku_stacks.ku_ring.ui.home.category

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.adapter.textColorGrayIf
import com.ku_stacks.ku_ring.adapter.visibleIf
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.ItemNoticeBinding

class NoticeViewHolder(
    private val binding: ItemNoticeBinding,
    private val itemClick: (Notice) -> (Unit),
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(notice: Notice) {
        binding.noticeItem = notice
        setupTag(notice.tag)
        binding.root.setOnClickListener {
            binding.noticeTitleTxt.textColorGrayIf(true)
            binding.noticeDateTxt.textColorGrayIf(true)
            binding.noticeIsNewPoint.visibleIf(false)
            binding.executePendingBindings()
            itemClick(notice)
        }
        binding.executePendingBindings()
    }

    private fun setupTag(tagList: List<String>) {
        val colors: MutableList<IntArray> = arrayListOf()
        val color = intArrayOf(
            ContextCompat.getColor(binding.root.context, R.color.kus_secondary_gray), //tag background color
            Color.TRANSPARENT, //tag border color
            Color.WHITE, //tag text color
            ContextCompat.getColor(binding.root.context, R.color.kus_background) //tag selected background color
        )

        for (item in tagList) {
            colors.add(color)
        }
        binding.noticeTagContainer.setTags(tagList, colors)
    }
}