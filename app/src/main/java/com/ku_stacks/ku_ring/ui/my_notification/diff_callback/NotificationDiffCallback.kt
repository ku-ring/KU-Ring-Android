package com.ku_stacks.ku_ring.ui.my_notification.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDataUiModel
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDateHeaderUiModel

class NotificationDiffCallback : DiffUtil.ItemCallback<PushDataUiModel>() {
    override fun areItemsTheSame(oldItem: PushDataUiModel, newItem: PushDataUiModel): Boolean {
        return if (oldItem is PushContentUiModel && newItem is PushContentUiModel) {
            oldItem.articleId == newItem.articleId
        } else if (oldItem is PushDateHeaderUiModel && newItem is PushDateHeaderUiModel) {
            oldItem.postedDate == newItem.postedDate
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItem: PushDataUiModel, newItem: PushDataUiModel): Boolean {
        return if (newItem is PushContentUiModel && oldItem is PushContentUiModel) {
            oldItem.articleId == newItem.articleId
        } else if (oldItem is PushDateHeaderUiModel && newItem is PushDateHeaderUiModel) {
            oldItem.postedDate == newItem.postedDate
        } else {
            false
        }
    }
}