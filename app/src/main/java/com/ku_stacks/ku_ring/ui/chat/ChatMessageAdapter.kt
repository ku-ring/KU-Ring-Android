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
import com.ku_stacks.ku_ring.ui.chat.ui_model.*
import com.ku_stacks.ku_ring.ui.chat.viewholder.AdminViewHolder
import com.ku_stacks.ku_ring.ui.chat.viewholder.SealedChatViewHolder
import com.ku_stacks.ku_ring.ui.chat.viewholder.ReceiveViewHolder
import com.ku_stacks.ku_ring.ui.chat.viewholder.SendViewHolder

class ChatMessageAdapter(
    private val onErrorClick: (SentMessageUiModel) -> Unit
) : ListAdapter<ChatUiModel, SealedChatViewHolder>(MessageDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SealedChatViewHolder {
        return when (viewType) {
            CHAT_DATE -> {
                // TODO
                throw Exception("no such viewType : $viewType")
            }
            CHAT_RECEIVED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_receive, parent, false)
                val binding = ItemChatReceiveBinding.bind(view)
                ReceiveViewHolder(binding)
            }
            CHAT_SENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_send, parent, false)
                val binding = ItemChatSendBinding.bind(view)
                SendViewHolder(binding).apply {
                    binding.chatSendErrorIv.setOnClickListener {
                        val position = absoluteAdapterPosition.takeIf { it != NO_POSITION }
                            ?: return@setOnClickListener
                        onErrorClick(getItem(position) as SentMessageUiModel)
                    }
                }
            }
            CHAT_ADMIN -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_admin, parent, false)
                val binding = ItemChatAdminBinding.bind(view)
                AdminViewHolder(binding)
            }
            else -> {
                throw IllegalStateException("no such viewType : $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: SealedChatViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ReceiveViewHolder -> {
                holder.bind(item as ReceivedMessageUiModel)
            }
            is SendViewHolder -> {
                holder.bind(item as SentMessageUiModel)
            }
            is AdminViewHolder -> {
                holder.bind(item as AdminMessageUiModel)
            }
            //TODO : ChatDateViewHolder 추가
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ChatDateUiModel -> CHAT_DATE
            is ReceivedMessageUiModel -> CHAT_RECEIVED
            is SentMessageUiModel -> CHAT_SENT
            is AdminMessageUiModel -> CHAT_ADMIN
        }
    }

    companion object {
        const val CHAT_DATE = 1
        const val CHAT_RECEIVED = 2
        const val CHAT_SENT = 3
        const val CHAT_ADMIN = 4
    }

    private object MessageDiffCallback : DiffUtil.ItemCallback<ChatUiModel>() {
        override fun areItemsTheSame(oldItem: ChatUiModel, newItem: ChatUiModel): Boolean {
            return if (oldItem.messageId == null && newItem.messageId == null) {
                oldItem.timeStamp == newItem.timeStamp
            } else if (oldItem.messageId != null && oldItem.messageId != null) {
                oldItem.messageId == newItem.messageId
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: ChatUiModel, newItem: ChatUiModel): Boolean {
            return if (oldItem is ChatDateUiModel && newItem is ChatDateUiModel) {
                oldItem.timeStamp == newItem.timeStamp
            } else if (oldItem is ReceivedMessageUiModel && newItem is ReceivedMessageUiModel) {
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