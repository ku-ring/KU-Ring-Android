package com.ku_stacks.ku_ring.ui.home.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.FragmentHomeCategoryBinding
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeActivity
import com.ku_stacks.ku_ring.ui.home.HomeActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class HomeBaseFragment : Fragment() {
    protected val disposable = CompositeDisposable()

    protected lateinit var binding: FragmentHomeCategoryBinding
    protected lateinit var pagingAdapter: NoticePagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_category, container,false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        setupSwipeRefresh()
        observePagingState()
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
                startDetailActivity(notice)
            },
            onBindItem = { notice ->
                (activity as HomeActivity).insertNotice(notice.articleId, notice.category)
            }
        )

        binding.categoryRecyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pagingAdapter
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

    private fun startDetailActivity(notice: Notice) {
        val intent = Intent(requireActivity(), NoticeActivity::class.java).apply {
            putExtra(NoticeActivity.NOTICE_URL, notice.url)
            putExtra(NoticeActivity.NOTICE_ARTICLE_ID, notice.articleId)
            putExtra(NoticeActivity.NOTICE_CATEGORY, notice.category)
        }
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.anim_slide_right_enter,
            R.anim.anim_stay_exit
        )
    }

    override fun onDestroyView() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroyView()
    }
}