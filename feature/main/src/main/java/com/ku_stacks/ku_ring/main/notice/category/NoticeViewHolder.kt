package com.ku_stacks.ku_ring.main.notice.category

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.ItemNoticeBinding
import com.ku_stacks.ku_ring.ui_util.textColorGrayIf
import com.ku_stacks.ku_ring.ui_util.visibleIf

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
            binding.noticeIsNewPoint.visibleIf(notice.isSaved)
            binding.executePendingBindings()
            itemClick(notice)
        }
        binding.executePendingBindings()
    }

    private fun setupTag(tagList: List<String>) {
        val colors: MutableList<IntArray> = arrayListOf()
        val color = intArrayOf(
            ContextCompat.getColor(
                binding.root.context,
                R.color.kus_secondary_gray
            ), //tag background color
            Color.TRANSPARENT, //tag border color
            Color.WHITE, //tag text color
            ContextCompat.getColor(
                binding.root.context,
                R.color.kus_background
            ) //tag selected background color
        )

        for (item in tagList) {
            colors.add(color)
        }
        binding.noticeTagContainer.setTags(tagList, colors)
    }
}