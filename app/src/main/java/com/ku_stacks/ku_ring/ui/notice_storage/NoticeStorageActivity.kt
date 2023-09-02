package com.ku_stacks.ku_ring.ui.notice_storage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.ActivityNoticeStorageBinding
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import com.ku_stacks.ku_ring.util.makeDialog
import com.yeonkyu.HoldableSwipeHelper.HoldableSwipeHandler
import com.yeonkyu.HoldableSwipeHelper.SwipeButtonAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NoticeStorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeStorageBinding
    private val viewModel by viewModels<NoticeStorageViewModel>()
    private lateinit var storageAdapter: NoticeStorageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBinding()
        setView()
        setListAdapter()
        collectData()
    }

    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice_storage)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setView() {
        binding.backImage.setOnClickListener {
            finish()
        }
        binding.clearNoticesImage.setOnClickListener {
            makeDialog(title = "모두 삭제할까요?").setOnConfirmClickListener { viewModel.clearNotices() }
        }
    }

    private fun setListAdapter() {
        storageAdapter = NoticeStorageAdapter {
            startNoticeActivity(it)
            viewModel.updateNoticeAsReadOnStorage(it.articleId, it.category)
        }
        binding.notificationStorageRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@NoticeStorageActivity)
            adapter = storageAdapter
        }

        val removeDrawable =
            AppCompatResources.getDrawable(this, R.drawable.ic_bookmark_remove)!!
        HoldableSwipeHandler.Builder(this)
            .setSwipeButtonAction(object : SwipeButtonAction {
                override fun onClickFirstButton(absoluteAdapterPosition: Int) {
                    val notice = storageAdapter.currentList[absoluteAdapterPosition]
                    viewModel.deleteNotice(notice.articleId, notice.category)
                }
            })
            .setDirectionAsRightToLeft(false)
            .setBackgroundColor(getColor(R.color.kus_green))
            .setFirstItemDrawable(removeDrawable)
            .setDismissOnClickFirstItem(true)
            .setOnRecyclerView(binding.notificationStorageRecyclerview)
            .build()
    }

    private fun collectData() {
        lifecycleScope.launchWhenResumed {
            viewModel.savedNotices.collectLatest {
                storageAdapter.submitList(it)
            }
        }
    }

    private fun startNoticeActivity(notice: Notice) {
        val intent = NoticeWebActivity.createIntent(this, notice)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, NoticeStorageActivity::class.java)
            activity.startActivity(intent)
        }
    }
}