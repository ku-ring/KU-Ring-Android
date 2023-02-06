package com.ku_stacks.ku_ring.ui.my_notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.databinding.ActivityNotificationBinding
import com.ku_stacks.ku_ring.ui.edit_subscription.EditSubscriptionActivity
import com.ku_stacks.ku_ring.ui.main.MainActivity
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import com.ku_stacks.ku_ring.util.makeDialog
import com.ku_stacks.ku_ring.util.putNoticeWebActivityExtras
import com.yeonkyu.HoldableSwipeHelper.HoldableSwipeHandler
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

        viewModel.getMyNotificationList()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        binding.lifecycleOwner = this
    }

    private fun setupView() {
        binding.backImg.setOnClickListener {
            startMainActivity()
        }

        binding.notificationSetNotiBtn.setOnClickListener {
            analytics.click("set_notification btn", "NotificationActivity")
            val intent = Intent(this, EditSubscriptionActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }

        binding.deleteImg.setOnClickListener {
            makeDialog(description = getString(R.string.confirm_to_delete_notification))
                .setOnConfirmClickListener { viewModel.deleteAllPushDB() }
        }
    }

    private fun setupListAdapter() {
        notificationAdapter = NotificationAdapter(
            itemClick = {
                startNoticeActivity(it)
            },
            onBindItem = {
                viewModel.updateNotificationToBeOld(it.articleId)
            }
        )

        binding.notificationRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }

        HoldableSwipeHandler.Builder(this)
            .setSwipeButtonAction(object : SwipeButtonAction {
                override fun onClickFirstButton(absoluteAdapterPosition: Int) {
                    Timber.e("onClickDelete position : $absoluteAdapterPosition")
                    val pushContent = notificationAdapter.currentList[absoluteAdapterPosition]
                    if (pushContent is PushContentUiModel) {
                        viewModel.deletePushDB(pushContent.articleId)
                    }
                }
            })
            .setDismissOnClickFirstItem(true)
            .setOnRecyclerView(binding.notificationRecyclerview)
            .excludeFromHoldableViewHolder(NotificationAdapter.NOTIFICATION_DATE)
            .build()
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

    private fun startNoticeActivity(pushContent: PushContentUiModel) {
        val intent = Intent(this, NoticeWebActivity::class.java)
            .putNoticeWebActivityExtras(pushContent)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startMainActivity()
    }
}