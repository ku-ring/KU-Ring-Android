package com.ku_stacks.ku_ring.ui.feedback

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityFeedbackBinding
import com.ku_stacks.ku_ring.util.AppearanceAnimator
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
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.toastByResource.observe(this) {
            Toast.makeText(this, getString(it), Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
    }
}