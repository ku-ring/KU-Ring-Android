package com.ku_stacks.ku_ring.ui.my_notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.databinding.ActivityNotificationBinding
import com.ku_stacks.ku_ring.ui.detail.DetailActivity
import com.ku_stacks.ku_ring.ui.home.HomeActivity
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.ui.setting_notification.SettingNotificationActivity
import com.yeonkyu.HoldableSwipeHelper.HoldableSwipeHelper
import com.yeonkyu.HoldableSwipeHelper.SwipeButtonAction
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
        setupView()
        setupListAdapter()
        observeData()

        viewModel.getMyNotification()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        binding.lifecycleOwner = this
    }

    private fun setupView() {
        binding.backImg.setOnClickListener {
            startHomeActivity()
        }

        binding.notificationSetNotiBtn.setOnClickListener {
            analytics.click("set_notification btn", "NotificationActivity")
            val intent = Intent(this, SettingNotificationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }

        binding.deleteImg.setOnClickListener {
            Timber.e("delete pushDB")
            viewModel.deleteAllPushDB()
        }
    }

    private fun setupListAdapter() {
        notificationAdapter = NotificationAdapter(
            itemClick = {
                viewModel.updateNoticeTobeRead(it.articleId, it.category)
                startDetailActivity(it.articleId, it.baseUrl, it.category)
            },
            onBindItem = {
                viewModel.updateNotification(it.articleId)
            }
        )

        binding.notificationRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }

        val swipeHelper = HoldableSwipeHelper(this, object : SwipeButtonAction {
            override fun onClickFirstButton(absoluteAdapterPosition: Int) {
                Timber.e("onClickDelete position : $absoluteAdapterPosition")
                val pushContent = notificationAdapter.currentList[absoluteAdapterPosition]
                if (pushContent is PushContentUiModel) {
                    viewModel.deletePushDB(pushContent.articleId)
                }
            }
        })

        swipeHelper.setDismissBackgroundOnClickedFirstItem(true)
        swipeHelper.addRecyclerViewListener(binding.notificationRecyclerview)
        swipeHelper.addRecyclerViewDecoration(binding.notificationRecyclerview)
        swipeHelper.excludeFromHoldableViewHolder(NotificationAdapter.NOTIFICATION_DATE)

        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.notificationRecyclerview)
    }

    private fun observeData() {
        viewModel.pushUiModelList.observe(this) {
            notificationAdapter.submitList(it)
            if (it.isEmpty()) {
                binding.notificationAlertTxt.visibility = View.VISIBLE
            } else {
                binding.notificationAlertTxt.visibility = View.GONE
            }
            refreshAdviceMessage()
        }
    }

    private fun refreshAdviceMessage() {
        if (binding.notificationAlertTxt.visibility == View.VISIBLE) {
            binding.notificationAlertTxt.text = if (viewModel.hasSubscribingNotification()) {
                getString(R.string.notification_alert)
            } else {
                getString(R.string.notification_no_subscription)
            }
        }
    }

    private fun startDetailActivity(articleId: String, baseUrl: String, category: String) {
        val url = if (category == "도서관") {
            "$baseUrl/$articleId"
        } else {
            "$baseUrl?id=$articleId"
        }
        Timber.e("url : $url, category : $category")

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startHomeActivity()
    }
}