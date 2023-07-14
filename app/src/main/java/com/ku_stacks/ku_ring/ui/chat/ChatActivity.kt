package com.ku_stacks.ku_ring.ui.chat

import android.content.ClipData
import android.content.ClipboardManager
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
import com.ku_stacks.ku_ring.ui.chat.ui_model.ReceivedMessageUiModel
import com.ku_stacks.ku_ring.ui.chat.ui_model.SentMessageUiModel
import com.ku_stacks.ku_ring.ui.dialogs.ChatActionDialog
import com.ku_stacks.ku_ring.util.makeDialog
import com.ku_stacks.ku_ring.util.modified_external_library.RecyclerViewPager
import com.ku_stacks.ku_ring.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel by viewModels<ChatViewModel>()

    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private lateinit var recyclerObserver: ChatRecyclerDataObserver
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
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }

        binding.chatSendBt.setOnClickListener {
            val message = binding.chatMessageEt.text.toString()
            binding.chatMessageEt.text.clear()
            viewModel.sendMessage(message)
        }
        binding.chatMessageEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                val textTintColor = if (s.isNullOrEmpty()) {
                    R.color.kus_green_50
                } else {
                    R.color.kus_green
                }
                ImageViewCompat.setImageTintList(
                    binding.chatSendBt,
                    ColorStateList.valueOf(getColor(textTintColor))
                )

                s?.length?.let { len ->
                    val maxLen = 300
                    if (len > maxLen) {
                        showToast(getString(R.string.chat_max_message_length, maxLen.toString()))
                        binding.chatMessageEt.setText(s.substring(0, maxLen))
                        binding.chatMessageEt.setSelection(binding.chatMessageEt.length())
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        chatMessageAdapter = ChatMessageAdapter(
            onErrorClick = { sentMessageUiModel -> makeResendDialog(sentMessageUiModel) },
            onMessageLongClick = { receivedMessageUiModel ->
                makeChatActionDialog(
                    receivedMessageUiModel
                )
            }
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

        viewModel.toastEvent.observe(this) {
            showToast(getString(it))
        }

        viewModel.readyToBottomScrollEvent.observe(this) {
            recyclerObserver.readyToBottomScroll(true)
        }
    }

    private fun makeResendDialog(sentMessageUiModel: SentMessageUiModel) {
        makeDialog(
            description = getString(R.string.chat_resend_message),
            leftText = getString(R.string.chat_delete),
            rightText = getString(R.string.chat_resend)
        ).setOnCancelClickListener {
            viewModel.deletePendingMessage(sentMessageUiModel)
        }.setOnConfirmClickListener {
            viewModel.deletePendingMessage(sentMessageUiModel)
            viewModel.sendMessage(sentMessageUiModel.message)
        }
    }

    private fun makeChatActionDialog(messageUiModel: ReceivedMessageUiModel) {
        ChatActionDialog(this).apply {
            show()
        }.setOnCopyMessageClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("message", messageUiModel.message)
            clipboardManager.setPrimaryClip(clipData)
            showToast(getString(R.string.chat_copied_message))
        }.setOnCopyNicknameClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("nickname", messageUiModel.nickname)
            clipboardManager.setPrimaryClip(clipData)
            showToast(getString(R.string.chat_copied_nickname))
        }.setOnReportClickListener {
            makeDialog(description = getString(R.string.report_ask_again))
                .setOnConfirmClickListener {
                    viewModel.reportMessage(messageUiModel)
                }
        }.setOnBlockClickListener {
            makeDialog(description = getString(R.string.block_ask_again))
                .setOnConfirmClickListener {
                    viewModel.blockUser(messageUiModel)
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}