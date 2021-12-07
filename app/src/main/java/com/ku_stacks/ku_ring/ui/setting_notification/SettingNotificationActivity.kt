package com.ku_stacks.ku_ring.ui.setting_notification

import android.os.Bundle
import android.view.View
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
        setupView()
        setupListAdapter()
        observeData()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_notification)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.settingNotificationDismissBt.setOnClickListener {
            if(viewModel.hasUpdate.value == true) {
                viewModel.saveSubscribe()
            }
            finish()
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }
        binding.settingNotificationRollbackBt.setOnClickListener {
            viewModel.syncWithServer()
        }
        binding.settingNotificationStartBt.setOnClickListener {
            if (viewModel.isSubscriptionEmpty.value == false) {
                viewModel.saveSubscribe()
                setResult(RESULT_OK)
                finish()
                overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
            }
        }
    }

    private fun setupView() {
        viewModel.firstRunFlag = intent.getBooleanExtra("firstRunFlag", false)
        if(viewModel.firstRunFlag) {
            binding.settingNotificationDismissBt.visibility = View.GONE
            binding.settingNotificationRollbackBt.visibility = View.GONE
            binding.settingNotificationStartBt.visibility = View.VISIBLE
        } else {
            binding.settingNotificationStartBt.visibility = View.GONE
        }

    }

    private fun setupListAdapter() {
        subscribeAdapter = SubscribeAdapter {
            viewModel.removeSubscription(it)
            viewModel.addUnSubscription(it)
            viewModel.refreshAfterUpdate()
        }
        unSubscribeListAdapter = UnSubscribeAdapter {
            viewModel.removeUnSubscription(it)
            viewModel.addSubscription(it)
            viewModel.refreshAfterUpdate()
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
            viewModel.isSubscriptionEmpty.postValue(it.isNullOrEmpty())
        }

        viewModel.unSubscriptionList.observe(this) {
            unSubscribeListAdapter.submitList(it.toList())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}