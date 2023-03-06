package com.ku_stacks.ku_ring.ui.chat

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Reference : https://github.com/sendbird/sendbird-chat-sample-android/blob/main/commonmodule/src/main/java/com/sendbird/chat/module/utils/ChatRecyclerDataObserver.kt

class ChatRecyclerDataObserver(
    private val recyclerView: RecyclerView
) : RecyclerView.AdapterDataObserver() {

    private var scrollToBottom: Boolean = false
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private var layoutManager: LinearLayoutManager

    init {
        if (recyclerView.adapter == null) {
            throw IllegalStateException("recyclerViewAdapter must be set")
        }
        adapter = recyclerView.adapter!!
        layoutManager = recyclerView.layoutManager as LinearLayoutManager
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        // 이미 가장 하단 메세지를 보고 있던 경우
        if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 2) {
            notifyUpdate()
        } else {
            // 스크롤을 강제로 내려야 하는 경우(메세지 전송 등)
            if (scrollToBottom) {
                notifyUpdate()
            }
        }
        super.onItemRangeInserted(positionStart, itemCount)
    }

    private fun notifyUpdate() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
        scrollToBottom = false
    }

    fun readyToBottomScroll(value: Boolean) {
        scrollToBottom = value
    }
}