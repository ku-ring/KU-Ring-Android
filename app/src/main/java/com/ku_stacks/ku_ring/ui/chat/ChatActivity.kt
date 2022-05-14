package com.ku_stacks.ku_ring.ui.chat

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityChatBinding
import com.ku_stacks.ku_ring.ui.chat.ui_model.SentMessageUiModel
import com.ku_stacks.ku_ring.util.makeDialog
import com.ku_stacks.ku_ring.util.modified_external_library.RecyclerViewPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel by viewModels<ChatViewModel>()

    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private lateinit var recyclerObserver: ChatRecyclerDataObserver
    private lateinit var pager: RecyclerViewPager

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupRecyclerView() {
        chatMessageAdapter = ChatMessageAdapter(
            onErrorClick = { makeResendDialog(it) }
        )

        binding.chatRecyclerview.apply {
            adapter = chatMessageAdapter
            pager = RecyclerViewPager(
                recyclerView = this,
                isReversed = true,
                isLoading = { viewModel.isLoading.value == true },
                loadNext = { viewModel.fetchPreviousMessageList(chatMessageAdapter.currentList.first().timeStamp) },
                isEnd = { viewModel.hasPrevious.value == false }
            )
            pager.prefetchDistance = 30
        }

        recyclerObserver = ChatRecyclerDataObserver(
            recyclerView = binding.chatRecyclerview
        )
        chatMessageAdapter.registerAdapterDataObserver(recyclerObserver)
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

        viewModel.readyToBottomScrollEvent.observe(this) {
            recyclerObserver.readyToBottomScroll(true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun makeResendDialog(sentMessageUiModel : SentMessageUiModel) {
        makeDialog(
            description = getString(R.string.chat_resend_message),
            leftText = getString(R.string.chat_delete),
            rightText = getString(R.string.chat_resend)
        )
            .setOnCancelClickListener {
                viewModel.deletePendingMessage(sentMessageUiModel)
            }
            .setOnConfirmClickListener {
                viewModel.deletePendingMessage(sentMessageUiModel)
                viewModel.sendMessage(sentMessageUiModel.message)
            }
    }
}