package com.ku_stacks.ku_ring.ui.feedback

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.databinding.ActivityFeedbackBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {
    @Inject
    lateinit var analytics : EventAnalytics

    private lateinit var binding: ActivityFeedbackBinding
    private val viewModel by viewModels<FeedbackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feedback)

    }
}