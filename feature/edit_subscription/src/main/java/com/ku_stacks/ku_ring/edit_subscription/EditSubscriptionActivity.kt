package com.ku_stacks.ku_ring.edit_subscription

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_subscription.compose.EditSubscriptionScreen
import com.ku_stacks.ku_ring.edit_subscription.databinding.ActivityEditSubscriptionBinding
import com.ku_stacks.ku_ring.thirdparty.compose.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSubscriptionBinding
    private val viewModel by viewModels<EditSubscriptionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_subscription)
        binding.lifecycleOwner = this
    }

    private fun setupView() {
        viewModel.firstRunFlag = intent.getBooleanExtra(FIRST_RUN_FLAG, false)
        binding.composeView.setContent {
            KuringCompositionLocalProvider {
                val navigator = LocalNavigator.current
                KuringTheme {
                    EditSubscriptionScreen(
                        viewModel = viewModel,
                        onNavigateToBack = ::finish,
                        onAddDepartmentButtonClick = { navigator.navigateToEditSubscribedDepartment(this) },
                        onSubscriptionComplete = ::onSubscriptionComplete,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    private fun onSubscriptionComplete() {
        if (viewModel.isInitialLoadDone) {
            viewModel.saveSubscribe()
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val FIRST_RUN_FLAG = "firstRunFlag"
        fun start(activity: Activity, isFirstRun: Boolean) {
            val intent = Intent(activity, EditSubscriptionActivity::class.java).apply {
                putExtra(FIRST_RUN_FLAG, isFirstRun)
            }
            activity.startActivity(intent)
        }
    }
}