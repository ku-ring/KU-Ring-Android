package com.ku_stacks.ku_ring.my_notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ku_stacks.ku_ring.my_notification.databinding.ItemDateBinding
import com.ku_stacks.ku_ring.my_notification.databinding.ItemNotificationBinding
import com.ku_stacks.ku_ring.my_notification.diff_callback.NotificationDiffCallback
import com.ku_stacks.ku_ring.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.my_notification.ui_model.PushDataUiModel
import com.ku_stacks.ku_ring.my_notification.ui_model.PushDateHeaderUiModel
import com.ku_stacks.ku_ring.my_notification.viewholder.DateViewHolder
import com.ku_stacks.ku_ring.my_notification.viewholder.NotificationViewHolder

class NotificationAdapter(
    private val itemClick: (PushContentUiModel) -> Unit,
    private val onBindItem: (PushContentUiModel) -> Unit
) : ListAdapter<PushDataUiModel, NotificationAdapter.ViewHolder>(
    NotificationDiffCallback()
) {
    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NOTIFICATION_CONTENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification, parent, false)
                val binding = ItemNotificationBinding.bind(view)
                NotificationViewHolder(binding, itemClick)
            }

            NOTIFICATION_DATE -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
                val binding = ItemDateBinding.bind(view)
                DateViewHolder(binding)
            }

            else -> throw Exception("no such viewType : $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is NotificationViewHolder -> {
                holder.bind(item as PushContentUiModel)
                onBindItem(item)
            }

            is DateViewHolder -> {
                holder.bind(item as PushDateHeaderUiModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PushContentUiModel -> NOTIFICATION_CONTENT
            is PushDateHeaderUiModel -> NOTIFICATION_DATE
        }
    }

    companion object {
        const val NOTIFICATION_CONTENT = 10
        const val NOTIFICATION_DATE = 11
    }
}