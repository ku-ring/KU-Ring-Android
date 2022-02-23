package com.ku_stacks.ku_ring.ui.my_notification.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.NotificationUiModel

class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationUiModel>() {
    override fun areItemsTheSame(oldItem: Push, newItem: Push): Boolean {
        val areSame = oldItem.articleId == newItem.articleId
        if (areSame) {
            newItem.isNew = oldItem.isNew
        }
        return areSame
    }

    override fun areContentsTheSame(oldItem: Push, newItem: Push): Boolean {
        return oldItem.articleId == newItem.articleId
                && oldItem.isNewDay == newItem.isNewDay
    }
}