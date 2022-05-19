package com.ku_stacks.ku_ring.ui.chat.viewholder

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.adapter.visibleIf
import com.ku_stacks.ku_ring.databinding.ItemChatReceiveBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.ReceivedMessageUiModel
import com.sendbird.android.SendbirdChat

class ReceiveViewHolder(
    private val binding: ItemChatReceiveBinding
) : SealedChatViewHolder(binding.root) {

    fun bind(receivedMessageUiModel: ReceivedMessageUiModel, showDate: Boolean) {
        binding.receivedMessageUiModel = receivedMessageUiModel
        binding.chatDateTv.visibleIf(showDate)

        binding.chatContentTv.text = spanNickname(
            message = receivedMessageUiModel.message,
            nickname = SendbirdChat.currentUser?.nickname ?: ""
        )
    }

    private fun spanNickname(message: String, nickname: String): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(message)

        val nicknameIndexList = getNicknameStartPositionList(message, nickname)
        nicknameIndexList.forEach { index ->
            spannableStringBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                index,
                index + nickname.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannableStringBuilder.setSpan(
                BackgroundColorSpan(binding.root.context.getColor(R.color.kus_green_50)),
                index,
                index + nickname.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannableStringBuilder
    }

    private fun getNicknameStartPositionList(message: String, nickname: String): List<Int> {
        if (nickname.isEmpty()) {
            return emptyList()
        }

        val indexList = mutableListOf<Int>()
        var index = 0
        while (index <= message.length - nickname.length) {
            var isNickname = true
            nickname.forEachIndexed { i, char ->
                if (message[index + i] != char) {
                    isNickname = false
                    return@forEachIndexed
                }
            }
            if (isNickname) {
                indexList.add(index)
                index += nickname.length
            } else {
                index++
            }
        }
        return indexList
    }
}