package com.ku_stacks.ku_ring.main.search.fragment_notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.mapper.toWebViewNotice
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.FragmentSearchNoticeBinding
import com.ku_stacks.ku_ring.main.search.SearchFragment
import com.ku_stacks.ku_ring.main.search.SearchViewModel
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Deprecated("SearchActivity 완성되면 제거")
@AndroidEntryPoint
class SearchNoticeFragment : Fragment() {

    private lateinit var binding: FragmentSearchNoticeBinding
    private val searchViewModel by viewModels<SearchViewModel>({ requireParentFragment() })
    private lateinit var searchNoticeAdapter: SearchNoticeAdapter

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_notice, container, false)
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
        // TODO : 추후 제거 (컴파일 에러로 주석처리)
//        searchViewModel.noticeList.observe(viewLifecycleOwner) {
//            searchNoticeAdapter.submitList(it)
//            if (it.isEmpty()) {
//                (parentFragment as SearchFragment).showAdviceText()
//            } else {
//                (parentFragment as SearchFragment).hideAdviceText()
//            }
//        }
    }

    private fun startNoticeActivity(notice: Notice) {
        navigator.navigateToNoticeWeb(requireActivity(), notice.toWebViewNotice())
    }
}
