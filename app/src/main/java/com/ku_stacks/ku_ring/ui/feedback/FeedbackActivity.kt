package com.ku_stacks.ku_ring.ui.feedback

import android.app.Service
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityFeedbackBinding
import com.ku_stacks.ku_ring.util.AppearanceAnimation
import com.ku_stacks.ku_ring.util.SoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private val viewModel by viewModels<FeedbackViewModel>()

    private var softKeyboard: SoftKeyboard? = null

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
    }

    private fun setupKeyboardListener() {
        val controlManager = getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        val softKeyboard = SoftKeyboard(binding.feedbackLayout, controlManager)
        softKeyboard.setSoftKeyboardCallback(object: SoftKeyboard.SoftKeyboardChanged {
            override fun onSoftKeyboardHide() {
                Timber.e("on keyboard hide")

                lifecycleScope.launch {
                    AppearanceAnimation.expand(binding.feedbackChatImg)
                    AppearanceAnimation.expand(binding.feedbackTitleTxt)
                }
            }

            override fun onSoftKeyboardShow() {
                Timber.e("on keyboard show")

                lifecycleScope.launch {
                    AppearanceAnimation.collapse(binding.feedbackChatImg)
                    AppearanceAnimation.collapse(binding.feedbackTitleTxt)
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        softKeyboard?.unRegisterSoftKeyboardCallback()
    }
}