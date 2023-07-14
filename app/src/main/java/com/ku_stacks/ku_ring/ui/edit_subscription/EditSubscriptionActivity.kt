package com.ku_stacks.ku_ring.ui.edit_subscription

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivityEditSubscriptionBinding
import com.ku_stacks.ku_ring.ui.edit_subscription.compose.Subscriptions
import com.ku_stacks.ku_ring.ui.compose.theme.KuringTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        binding.viewModel = viewModel

        binding.dismissBt.setOnClickListener {
            if (viewModel.hasUpdate.value) {
                viewModel.saveSubscribe()
            }
            finish()
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }
        binding.rollbackBt.setOnClickListener {
            viewModel.rollback()
        }
        binding.startBt.setOnClickListener {
            Timber.d("Init status = ${viewModel.isInitialLoadDone}")
            if (viewModel.isInitialLoadDone) {
                viewModel.saveSubscribe()
                setResult(RESULT_OK)
                finish()
                overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
            }
        }
    }

    private fun setupView() {
        viewModel.firstRunFlag = intent.getBooleanExtra(FIRST_RUN_FLAG, false)
        if (viewModel.firstRunFlag) {
            binding.dismissBt.visibility = View.GONE
            binding.rollbackBt.visibility = View.GONE
            binding.startBt.visibility = View.VISIBLE
        } else {
            binding.startBt.visibility = View.GONE
        }
        binding.composeView.setContent {
            val categoryTitle = stringResource(R.string.subscribe_category_title)
            val departmentTitle = stringResource(R.string.subscribe_department_title)
            val categories by viewModel.sortedCategories.collectAsState(initial = emptyList())
            val departments by viewModel.sortedDepartments.collectAsState(initial = emptyList())
            KuringTheme {
                Subscriptions(
                    categories = categories,
                    categoriesHeaderTitle = categoryTitle,
                    departments = departments,
                    departmentsHeaderTitle = departmentTitle,
                    onItemClick = viewModel::onItemClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val FIRST_RUN_FLAG = "firstRunFlag"
    }
}