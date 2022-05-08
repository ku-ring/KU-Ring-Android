package com.ku_stacks.ku_ring.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityChatBinding
import com.ku_stacks.ku_ring.util.makeDialog
import com.ku_stacks.ku_ring.util.modified_external_library.RecyclerViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel by viewModels<ChatViewModel>()

    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private lateinit var pager: RecyclerViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
        setupRecyclerView()
        observeData()
        observeEvent()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        binding.lifecycleOwner = this
    }

    private fun setupView() {
        binding.chatBackBt.setOnClickListener {
            finish()
        }
        binding.chatSendBt.setOnClickListener {
            val message = binding.chatMessageEt.text.toString()
            binding.chatMessageEt.text.clear()
            viewModel.sendMessage(message)
        }
    }

    private fun setupRecyclerView() {
        chatMessageAdapter = ChatMessageAdapter()

        binding.chatRecyclerview.apply {
            adapter = chatMessageAdapter
//            pager = RecyclerViewPager(
//                recyclerView = this,
//                isLoading = { false },
//                loadNext = { viewModel.fetchPreviousMessageList(0) }, //TODO : not zero
//                isEnd = { viewModel.hasPrevious.value == false }
//            )
        }
    }

    private fun observeData() {
        viewModel.chatUiModelList.observe(this) {
            chatMessageAdapter.submitList(it)
        }
    }

    private fun observeEvent() {
        viewModel.dialogEvent.observe(this) {
            makeDialog(description = getString(it))
        }
    }
}