package com.ku_stacks.ku_ring.ui.search.fragment_notice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.databinding.FragmentSearchNoticeBinding
import com.ku_stacks.ku_ring.ui.detail.DetailActivity
import com.ku_stacks.ku_ring.ui.search.SearchActivity
import com.ku_stacks.ku_ring.ui.search.SearchViewModel
import timber.log.Timber

class SearchNoticeFragment: Fragment() {

    private lateinit var binding: FragmentSearchNoticeBinding
    private val searchViewModel by activityViewModels<SearchViewModel>()
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
        searchNoticeAdapter = SearchNoticeAdapter {
            searchViewModel.updateNoticeTobeRead(it)
            startDetailActivity(it)
        }
        binding.searchNoticeRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.searchNoticeRecyclerview.adapter = searchNoticeAdapter
    }

    private fun observeData() {
        searchViewModel.noticeList.observe(viewLifecycleOwner) {
            searchNoticeAdapter.submitList(it)
            if (it.isEmpty()) {
                (activity as SearchActivity).showAdviceText()
            } else {
                (activity as SearchActivity).hideAdviceText()
            }
        }
    }

    private fun startDetailActivity(notice: Notice) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("url", notice.url)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }
}