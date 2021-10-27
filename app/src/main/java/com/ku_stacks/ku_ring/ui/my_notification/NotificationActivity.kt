package com.ku_stacks.ku_ring.ui.my_notification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    @Inject
    lateinit var analytics : EventAnalytics

    private lateinit var binding: ActivityNotificationBinding
    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListAdapter()
        observeData()

        viewModel.getMyNotification()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        binding.lifecycleOwner = this

        binding.backImg.setOnClickListener {
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
            finish()
        }

        binding.notificationSetNotiBtn.setOnClickListener {
            analytics.click("set_notification btn", "NotificationActivity")
            // TODO create new activity
        }
    }

    private fun setupListAdapter() {
        notificationAdapter = NotificationAdapter (
            { Snackbar.make(binding.root, "push noti click", Snackbar.LENGTH_SHORT).show() },
            { it -> viewModel.updateNotification(it.articleId) }
        )

        binding.notificationRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }
    }

    private fun observeData() {
        viewModel.pushList.observe(this) {
            notificationAdapter.submitList(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}