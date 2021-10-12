package com.ku_stacks.ku_ring.ui.feedback

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityFeedbackBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private val viewModel by viewModels<FeedbackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
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
}