package com.ku_stacks.ku_ring.feedback.feedback

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.feedback.feedback.compose.FeedbackScreen
import com.ku_stacks.ku_ring.ui_util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {

    private val viewModel by viewModels<FeedbackViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        setContent {
            KuringTheme {
                FeedbackScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.quit
            .flowWithLifecycle(lifecycle)
            .onEach {
                finish()
            }.launchIn(lifecycleScope)
        viewModel.toast
            .flowWithLifecycle(lifecycle)
            .onEach {
                showToast(it)
            }.launchIn(lifecycleScope)
        viewModel.toastByResource
            .flowWithLifecycle(lifecycle)
            .onEach {
                showToast(getString(it))
            }.launchIn(lifecycleScope)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, FeedbackActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }
}
