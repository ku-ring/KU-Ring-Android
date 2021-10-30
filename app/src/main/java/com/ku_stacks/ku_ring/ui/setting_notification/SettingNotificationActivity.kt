package com.ku_stacks.ku_ring.ui.setting_notification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySettingNotificationBinding
import com.ku_stacks.ku_ring.ui.setting_notification.adapter.SubscribeAdapter
import com.ku_stacks.ku_ring.ui.setting_notification.adapter.UnSubscribeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingNotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingNotificationBinding
    private val viewModel by viewModels<SettingNotificationViewModel>()

    private lateinit var subscribeAdapter: SubscribeAdapter
    private lateinit var unSubscribeListAdapter: UnSubscribeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupListAdapter()
        observeData()

        binding.sendBt.setOnClickListener {
            viewModel.saveSubscribe()
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_notification)
        binding.lifecycleOwner = this
    }

    private fun setupListAdapter() {
        subscribeAdapter = SubscribeAdapter {
            viewModel.removeSubscription(it)
            viewModel.addUnSubscription(it)
        }
        unSubscribeListAdapter = UnSubscribeAdapter {
            viewModel.removeUnSubscription(it)
            viewModel.addSubscription(it)
        }

        binding.subscribeRecyclerview.apply {
            layoutManager = GridLayoutManager(this@SettingNotificationActivity,
                3,
                GridLayoutManager.VERTICAL,
                false)
            adapter = subscribeAdapter
        }

        binding.unsubscribeRecyclerview.apply {
            layoutManager = GridLayoutManager(this@SettingNotificationActivity,
                3,
                GridLayoutManager.VERTICAL,
                false)
            adapter = unSubscribeListAdapter
        }
    }

    private fun observeData() {
        viewModel.subscriptionList.observe(this) {
            subscribeAdapter.submitList(it.toList())
        }

        viewModel.unSubscriptionList.observe(this) {
            unSubscribeListAdapter.submitList(it.toList())
        }

        viewModel.quit.observe(this) {
            finish()
        }
    }
}