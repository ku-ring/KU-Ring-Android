package com.ku_stacks.ku_ring.ui.main.search.fragment_notice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.databinding.FragmentSearchNoticeBinding
import com.ku_stacks.ku_ring.ui.main.search.SearchFragment
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import com.ku_stacks.ku_ring.ui.main.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNoticeFragment: Fragment() {

    private lateinit var binding: FragmentSearchNoticeBinding
    private val searchViewModel by viewModels<SearchViewModel>({ requireParentFragment() })
    private lateinit var searchNoticeAdapter: SearchNoticeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_notice, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        observeData()
    }

    private fun setupListAdapter() {
        searchNoticeAdapter = SearchNoticeAdapter(
            itemClick = {
                startNoticeActivity(it)
            }
        )
        binding.searchNoticeRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.searchNoticeRecyclerview.adapter = searchNoticeAdapter
    }

    private fun observeData() {
        searchViewModel.noticeList.observe(viewLifecycleOwner) {
            searchNoticeAdapter.submitList(it)
            if (it.isEmpty()) {
                (parentFragment as SearchFragment).showAdviceText()
            } else {
                (parentFragment as SearchFragment).hideAdviceText()
            }
        }
    }

    private fun startNoticeActivity(notice: Notice) {
        val intent = Intent(requireContext(), NoticeWebActivity::class.java).apply {
            putExtra(NoticeWebActivity.NOTICE_URL, notice.url)
            putExtra(NoticeWebActivity.NOTICE_ARTICLE_ID, notice.articleId)
            putExtra(NoticeWebActivity.NOTICE_CATEGORY, notice.category)
        }
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }
}