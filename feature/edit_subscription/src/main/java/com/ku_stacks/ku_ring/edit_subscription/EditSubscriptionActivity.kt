package com.ku_stacks.ku_ring.edit_subscription

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.databinding.DataBindingUtil
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_subscription.compose.Subscriptions
import com.ku_stacks.ku_ring.edit_subscription.databinding.ActivityEditSubscriptionBinding
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

//        binding.startBt.setOnClickListener {
//            Timber.d("Init status = ${viewModel.isInitialLoadDone}")
//            if (viewModel.isInitialLoadDone) {
//                viewModel.saveSubscribe()
//                setResult(RESULT_OK)
//                finish()
//                overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
//            }
//        }
    }

    private fun setupView() {
        viewModel.firstRunFlag = intent.getBooleanExtra(FIRST_RUN_FLAG, false)
//        if (viewModel.firstRunFlag) {
//            binding.startBt.visibility = View.VISIBLE
//        } else {
//            binding.startBt.visibility = View.GONE
//        }
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
                    modifier = Modifier.fillMaxSize()
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
        fun start(activity: Activity, isFirstRun: Boolean) {
            val intent = Intent(activity, EditSubscriptionActivity::class.java).apply {
                putExtra(FIRST_RUN_FLAG, isFirstRun)
            }
            activity.startActivity(intent)
        }
    }
}