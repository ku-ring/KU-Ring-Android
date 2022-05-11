package com.ku_stacks.ku_ring.ui.chat

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
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
        binding.viewModel = viewModel
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
        binding.chatMessageEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) { // modify tint color
                    ImageViewCompat.setImageTintList(binding.chatSendBt, ColorStateList.valueOf(getColor(R.color.kus_green_50)))
                } else {
                    ImageViewCompat.setImageTintList(binding.chatSendBt, ColorStateList.valueOf(getColor(R.color.kus_green)))
                }
            }
        })
    }

    private fun setupRecyclerView() {
        chatMessageAdapter = ChatMessageAdapter()

        binding.chatRecyclerview.apply {
            adapter = chatMessageAdapter
            pager = RecyclerViewPager(
                recyclerView = this,
                isReversed = true,
                isLoading = { viewModel.isLoading.value == true },
                loadNext = { viewModel.fetchPreviousMessageList(chatMessageAdapter.currentList.first().timeStamp) },
                isEnd = { viewModel.hasPrevious.value == false }
            )
        }
    }

    private fun observeData() {
        viewModel.chatUiModelList.observe(this) {
            chatMessageAdapter.submitList(it.toList()) {
                viewModel.isLoading.postValue(false)
            }
        }
    }

    private fun observeEvent() {
        viewModel.dialogEvent.observe(this) {
            makeDialog(description = getString(it))
        }
    }
}