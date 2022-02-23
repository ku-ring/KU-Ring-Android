package com.ku_stacks.ku_ring.ui.my_notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.databinding.ItemDateBinding
import com.ku_stacks.ku_ring.databinding.ItemNotificationBinding
import com.ku_stacks.ku_ring.ui.my_notification.diff_callback.NotificationDiffCallback
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.NotificationUiModel
import com.ku_stacks.ku_ring.ui.my_notification.viewholder.DateViewHolder
import com.ku_stacks.ku_ring.ui.my_notification.viewholder.NotificationViewHolder
import timber.log.Timber

class NotificationAdapter(
    private val itemClick: (Push) -> Unit,
    private val onBindItem: (Push) -> Unit
): ListAdapter<NotificationUiModel, NotificationAdapter.ViewHolder>(
    NotificationDiffCallback()
) {
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(any: Any) = Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NOTIFICATION_CONTENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
                val binding = ItemNotificationBinding.bind(view)
                NotificationViewHolder(binding, itemClick)
            }
            NOTIFICATION_DATE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
                val binding = ItemDateBinding.bind(view)
                DateViewHolder(binding)
            }
            else -> throw Exception("no such viewType : $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder.itemViewType) {
            NOTIFICATION_CONTENT -> {
                (holder as NotificationViewHolder).bind(item)
                onBindItem(item)
            }
            NOTIFICATION_DATE -> {
                val isNewDay = true
                (holder as DateViewHolder).bind(item, isNewDay)
            }
            else -> {
                Timber.e("no such viewType : ${holder.itemViewType}")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

    }

    companion object {
        const val NOTIFICATION_CONTENT = 10
        const val NOTIFICATION_DATE = 11
    }
}