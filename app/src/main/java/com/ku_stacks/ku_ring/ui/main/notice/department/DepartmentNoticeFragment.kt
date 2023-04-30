package com.ku_stacks.ku_ring.ui.main.notice.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentDepartmentNoticeBinding
import com.ku_stacks.ku_ring.ui.main.notice.category.NoticePagingAdapter
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DepartmentNoticeFragment : Fragment() {

    private lateinit var binding: FragmentDepartmentNoticeBinding
    private lateinit var pagingAdapter: NoticePagingAdapter

    private val departmentNoticeViewModel by viewModels<DepartmentNoticeViewModel>()

    private val shortName = "cse"

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

        setupListAdapter()
        setupSwipeRefresh()
        observePagingState()
        collectNotices()
    }

    private fun observePagingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showShimmerView()
                    }
                    is LoadState.NotLoading -> {
                        hideShimmerView()
                    }
                    is LoadState.Error -> {
                        //TODO error
                    }
                }
            }
        }
    }

    private fun setupListAdapter() {
        pagingAdapter = NoticePagingAdapter(
            itemClick = { notice ->
                val intent = NoticeWebActivity.createIntent(this.requireContext(), notice)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.anim_slide_right_enter,
                    R.anim.anim_stay_exit
                )
            },
            onBindItem = { notice ->
//                noticesChildViewModel.insertNoticeAsOld(notice)
            }
        )

        binding.categoryRecyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pagingAdapter
        }
    }

    private fun collectNotices() {
        lifecycleScope.launch {
            departmentNoticeViewModel.getDepartmentNotices(shortName).collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.categorySwipeRefresh.setOnRefreshListener {
            binding.categorySwipeRefresh.isRefreshing = false
            pagingAdapter.refresh()
            showShimmerView()
        }
    }

    private fun showShimmerView() {
        binding.homeShimmerLayout.startShimmer()
        binding.categoryRecyclerview.visibility = View.GONE
        binding.homeShimmerLayout.visibility = View.VISIBLE
    }

    private fun hideShimmerView() {
        binding.homeShimmerLayout.stopShimmer()
        binding.categoryRecyclerview.visibility = View.VISIBLE
        binding.homeShimmerLayout.visibility = View.GONE
    }
}