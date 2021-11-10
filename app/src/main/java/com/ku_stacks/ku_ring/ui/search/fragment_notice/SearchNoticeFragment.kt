package com.ku_stacks.ku_ring.ui.search.fragment_notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentSearchNoticeBinding
import com.ku_stacks.ku_ring.ui.search.SearchViewModel

class SearchNoticeFragment: Fragment() {

    private lateinit var binding: FragmentSearchNoticeBinding
    private val searchViewModel by activityViewModels<SearchViewModel>()
    private lateinit var searchNoticeAdapter: SearchNoticeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_notice, container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        observeData()
    }

    private fun setupListAdapter() {
        searchNoticeAdapter = SearchNoticeAdapter()
        binding.searchNoticeRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.searchNoticeRecyclerview.adapter = searchNoticeAdapter
    }

    private fun observeData() {
        searchViewModel.noticeList.observe(viewLifecycleOwner) {
            searchNoticeAdapter.submitList(it)
        }
    }
}