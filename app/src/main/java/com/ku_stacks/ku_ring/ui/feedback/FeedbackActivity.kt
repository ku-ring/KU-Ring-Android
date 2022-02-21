package com.ku_stacks.ku_ring.ui.feedback

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityFeedbackBinding
import com.ku_stacks.ku_ring.util.AppearanceAnimator
import com.ku_stacks.ku_ring.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private val viewModel by viewModels<FeedbackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupKeyboardListener()
        setupEditText()
        observeData()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun observeData() {
        viewModel.quit.observe(this) {
            finish()
        }
        viewModel.toast.observe(this) {
            showToast(it)
        }
        viewModel.toastByResource.observe(this) {
            showToast(getString(it))
        }
    }

    private fun setupKeyboardListener() {
        KeyboardVisibilityEvent.setEventListener(this, this) { isOpen ->
            if (isOpen) {
                AppearanceAnimator.collapse(binding.feedbackChatImg)
                AppearanceAnimator.collapse(binding.feedbackTitleTxt)
            } else {
                val initialHeightOfFeedbackImg = 110
                AppearanceAnimator.expand(binding.feedbackChatImg, initialHeightOfFeedbackImg)
                AppearanceAnimator.expand(binding.feedbackTitleTxt)
            }
        }
    }

    private fun setupEditText() {
        binding.feedbackEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val strLength = s.toString().length
                binding.feedbackAdviceTxt.text = when {
                    strLength < 5 -> {
                        viewModel.canSendFeedback.postValue(false)
                        getString(R.string.feedback_write_more_character)
                    }
                    strLength > 256 -> {
                        viewModel.canSendFeedback.postValue(false)
                        String.format(getString(R.string.feedback_size_of_character), strLength)
                    }
                    else -> {
                        viewModel.canSendFeedback.postValue(true)
                        String.format(getString(R.string.feedback_size_of_character), strLength)
                    }
                }
            }
        })
    }
}