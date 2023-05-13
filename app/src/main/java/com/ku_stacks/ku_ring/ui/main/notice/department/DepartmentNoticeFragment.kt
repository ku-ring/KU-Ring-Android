package com.ku_stacks.ku_ring.ui.main.notice.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentDepartmentNoticeBinding
import com.ku_stacks.ku_ring.ui.compose.theme.KuringTheme
import com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe.DepartmentSubscribeBottomSheet
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
            DepartmentSubscribeBottomSheet.newInstance()
                .show(requireActivity().supportFragmentManager, DepartmentSubscribeBottomSheet::class.java.name)
        }
    }

    private fun setupNoticeListView() {
        binding.departmentNotices.setContent {
            KuringTheme {
                Text("여기에 학과 선택 및 공지 리스트가 들어갑니다.")
            }
        }
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
