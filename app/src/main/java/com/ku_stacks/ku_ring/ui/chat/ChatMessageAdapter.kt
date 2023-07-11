package com.ku_stacks.ku_ring.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ItemChatAdminBinding
import com.ku_stacks.ku_ring.databinding.ItemChatReceiveBinding
import com.ku_stacks.ku_ring.databinding.ItemChatSendBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.AdminMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.ChatUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.ReceivedMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.SentMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.viewholder.AdminViewHolder
import com.ku_stacks.ku_ring.ui.chat.viewholder.ReceiveViewHolder
import com.ku_stacks.ku_ring.ui.chat.viewholder.SealedChatViewHolder
import com.ku_stacks.ku_ring.ui.chat.viewholder.SendViewHolder
import com.ku_stacks.ku_ring.util.DateUtil.areSameDate

class ChatMessageAdapter(
    private val onErrorClick: (SentMessageUiModel) -> Unit,
    private val onMessageLongClick: (ReceivedMessageUiModel) -> Unit
) : ListAdapter<ChatUiModel, SealedChatViewHolder>(MessageDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SealedChatViewHolder {
        return when (viewType) {
            CHAT_RECEIVED -> {
                createReceivedViewHolder(parent)
            }

            CHAT_SENT -> {
                createSendViewHolder(parent)
            }

            CHAT_ADMIN -> {
                createAdminViewHolder(parent)
            }

            else -> {
                throw IllegalStateException("no such viewType : $viewType")
            }
        }
    }

    private fun createAdminViewHolder(parent: ViewGroup): AdminViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_admin, parent, false)
        val binding = ItemChatAdminBinding.bind(view)
        return AdminViewHolder(binding)
    }

    private fun createSendViewHolder(parent: ViewGroup): SendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_send, parent, false)
        val binding = ItemChatSendBinding.bind(view)
        return SendViewHolder(binding).apply {
            binding.chatSendErrorIv.setOnClickListener {
                val position = absoluteAdapterPosition.takeIf { it != NO_POSITION }
                    ?: return@setOnClickListener
                onErrorClick(getItem(position) as SentMessageUiModel)
            }
        }
    }

    private fun createReceivedViewHolder(parent: ViewGroup): ReceiveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_receive, parent, false)
        val binding = ItemChatReceiveBinding.bind(view)
        return ReceiveViewHolder(binding).apply {
            binding.chatMessageLayout.setOnLongClickListener {
                val position = absoluteAdapterPosition.takeIf { it != NO_POSITION }
                    ?: return@setOnLongClickListener false
                onMessageLongClick(getItem(position) as ReceivedMessageUiModel)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onBindViewHolder(holder: SealedChatViewHolder, position: Int) {
        val showDate = if (position > 0) {
            val cur = currentList[position].timeStamp
            val prev = currentList[position - 1].timeStamp
            !areSameDate(cur, prev)
        } else {
            true
        }

        val item = getItem(position)
        when (holder) {
            is ReceiveViewHolder -> {
                holder.bind(item as ReceivedMessageUiModel, showDate)
            }

            is SendViewHolder -> {
                holder.bind(item as SentMessageUiModel, showDate)
            }

            is AdminViewHolder -> {
                holder.bind(item as AdminMessageUiModel, showDate)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ReceivedMessageUiModel -> CHAT_RECEIVED
            is SentMessageUiModel -> CHAT_SENT
            is AdminMessageUiModel -> CHAT_ADMIN
        }
    }

    companion object {
        const val CHAT_RECEIVED = 1
        const val CHAT_SENT = 2
        const val CHAT_ADMIN = 3
    }

    private object MessageDiffCallback : DiffUtil.ItemCallback<ChatUiModel>() {
        override fun areItemsTheSame(oldItem: ChatUiModel, newItem: ChatUiModel): Boolean {
            // SentMessageUiModel 은 같은 말풍선에 대해서 messageId 가 (전송 시작) : 0, (전송 후) > 0 로 변하기 때문에 분기처리
            return if (oldItem is SentMessageUiModel && newItem is SentMessageUiModel) {
                if (oldItem.messageId > 0 && newItem.messageId > 0) {
                    oldItem.messageId == newItem.messageId
                } else {
                    oldItem.requestId == newItem.requestId
                }
            } else {
                oldItem.messageId == newItem.messageId
            }

        }

        override fun areContentsTheSame(oldItem: ChatUiModel, newItem: ChatUiModel): Boolean {
            return if (oldItem is ReceivedMessageUiModel && newItem is ReceivedMessageUiModel) {
                oldItem.messageId == newItem.messageId
                    && oldItem.message == newItem.message
            } else if (oldItem is SentMessageUiModel && newItem is SentMessageUiModel) {
                oldItem.messageId == newItem.messageId
                    && oldItem.message == newItem.message
                    && oldItem.isPending == newItem.isPending
            } else if (oldItem is AdminMessageUiModel && newItem is AdminMessageUiModel) {
                oldItem.messageId == newItem.messageId
                    && oldItem.message == newItem.message
            } else {
                false
            }
        }
    }
}