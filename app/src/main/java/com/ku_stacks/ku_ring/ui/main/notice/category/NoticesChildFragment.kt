package com.ku_stacks.ku_ring.ui.main.notice.category

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
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.FragmentHomeCategoryBinding
import com.ku_stacks.ku_ring.ui.main.notice.NoticeViewModel
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NoticesChildFragment : Fragment() {
    private val disposable = CompositeDisposable()

    private lateinit var binding: FragmentHomeCategoryBinding
    private lateinit var pagingAdapter: NoticePagingAdapter

    private val noticesChildViewModel by viewModels<NoticesChildViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_category, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        setupSwipeRefresh()
        observePagingState()
        subscribeNotices()
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
                startNoticeActivity(notice)
            },
            onBindItem = { notice ->
                noticesChildViewModel.insertNoticeAsOld(notice)
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

    private fun subscribeNotices() {
        val shortCategory = arguments?.getString(SHORT_CATEGORY)

        if (shortCategory.isNullOrEmpty()) {
            Timber.e("shortCategory is null")
            return
        }

        val noticeDisposable = noticesChildViewModel.getNotices(shortCategory).subscribe(
            { pagingAdapter.submitData(lifecycle, it) },
            { Timber.e("Subscribe error: $it") },
        )
        disposable.add(noticeDisposable)
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

    private fun startNoticeActivity(notice: Notice) {
        val intent = NoticeWebActivity.createIntent(requireActivity(), notice)
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

    companion object {
        private const val SHORT_CATEGORY = "SHORT_CATEGORY"

        fun newInstance(shortCategory: String): NoticesChildFragment {
            val args = Bundle().apply {
                putString(SHORT_CATEGORY, shortCategory)
            }
            return NoticesChildFragment().apply {
                arguments = args
            }
        }
    }
}
