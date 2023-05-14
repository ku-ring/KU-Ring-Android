package com.ku_stacks.ku_ring.ui.main.notice.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.FragmentDepartmentNoticeBinding
import com.ku_stacks.ku_ring.ui.compose.theme.KuringTheme
import com.ku_stacks.ku_ring.ui.main.notice.department.compose.DepartmentNoticeScreen
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import com.ku_stacks.ku_ring.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DepartmentNoticeFragment : Fragment() {

    private lateinit var binding: FragmentDepartmentNoticeBinding

    private val viewModel by viewModels<DepartmentNoticeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_department_notice, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddButtonClickListener()
        setupNoticeListView()
        observeLoadingState()
    }

    private fun setupAddButtonClickListener() {
        binding.addDepartmentButton.setOnClickListener {
            // TODO: 공지 검색 Fragment 연결하기
            requireContext().showToast("클릭!")
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun setupNoticeListView() {
        binding.departmentNotices.setContent {
            KuringTheme {
                val selectedDepartments by viewModel.subscribedDepartments.collectAsState()
                val noticesFlow by viewModel.currentDepartmentNotice.collectAsState()
                val notices = noticesFlow.collectAsLazyPagingItems()

                var isRefreshing by remember { mutableStateOf(false) }
                val refreshState = rememberPullRefreshState(
                    refreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        notices.refresh()
                        isRefreshing = false
                    },
                    refreshThreshold = 100.dp,
                )
                DepartmentNoticeScreen(
                    selectedDepartments = selectedDepartments,
                    onSelectDepartment = viewModel::selectDepartment,
                    notices = notices,
                    onNoticeClick = ::startNoticeActivity,
                    onFabClick = {},
                    isRefreshing = isRefreshing,
                    refreshState = refreshState,
                    modifier = Modifier
                        .background(colorResource(id = R.color.kus_background))
                        .padding(bottom = 56.dp)
                        .fillMaxSize(),
                    scope = rememberCoroutineScope(),
                )
            }
        }
    }

    private fun startNoticeActivity(notice: Notice) {
        val intent = NoticeWebActivity.createIntent(requireActivity(), notice)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.anim_slide_right_enter,
            R.anim.anim_stay_exit
        )
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.departmentNoticeScreenState.collectLatest { screenState ->
                when (screenState) {
                    is DepartmentNoticeScreenState.InitialLoading -> {
                        showShimmerView()
                        hideAddDepartmentButton()
                        hideNotices()
                    }

                    is DepartmentNoticeScreenState.DepartmentsEmpty -> {
                        hideShimmerView()
                        showAddDepartmentButton()
                        hideNotices()
                    }

                    is DepartmentNoticeScreenState.DepartmentsNotEmpty -> {
                        hideShimmerView()
                        hideAddDepartmentButton()
                        showNotices()
                    }
                }
            }
        }
    }

    private fun showShimmerView() {
        binding.homeShimmerLayout.startShimmer()
        binding.homeShimmerLayout.visibility = View.VISIBLE
        binding.departmentNotices.visibility = View.GONE
    }

    private fun hideShimmerView() {
        binding.homeShimmerLayout.stopShimmer()
        binding.homeShimmerLayout.visibility = View.GONE
    }

    private fun showAddDepartmentButton() {
        binding.addDepartmentButton.visibility = View.VISIBLE
    }

    private fun hideAddDepartmentButton() {
        binding.addDepartmentButton.visibility = View.GONE
    }

    private fun showNotices() {
        binding.departmentNotices.visibility = View.VISIBLE
    }

    private fun hideNotices() {
        binding.departmentNotices.visibility = View.GONE
    }
}