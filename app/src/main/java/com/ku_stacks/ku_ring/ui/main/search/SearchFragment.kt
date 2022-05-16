package com.ku_stacks.ku_ring.ui.main.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ku_stacks.ku_ring.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val searchViewModel by viewModels<SearchViewModel>()

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Timber.e("pageSelect detected, position : $position")
            when (position) {
                0 -> {
                    if (searchViewModel.noticeList.value?.isEmpty() == false) {
                        hideAdviceText()
                    } else {
                        showAdviceText()
                    }
                }
                1 -> {
                    if (searchViewModel.staffList.value?.isEmpty() == false) {
                        hideAdviceText()
                    } else {
                        showAdviceText()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFragment()
        setupView()
    }

    private fun setupFragment() {
        val pagerAdapter = SearchPagerAdapter(childFragmentManager, lifecycle)
        binding.searchViewpager.adapter = pagerAdapter
        binding.searchViewpager.registerOnPageChangeCallback(pageChangeCallback)
        TabLayoutMediator(binding.searchTabLayout, binding.searchViewpager, false) { tab, position ->
            when (position) {
                0 -> tab.text = "공지"
                1 -> tab.text = "교직원"
            }
        }.attach()
    }

    private fun setupView() {
        binding.searchKeywordEt.addTextChangedListener(object : TextWatcher {
            var lastEditTime = 0L

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                synchronized(this) {
                    lastEditTime = System.currentTimeMillis()
                }

                lifecycleScope.launch {
                    // 0.2초 후에 lastEditTime 이 변경되지 않았으면 검색
                    val now = lastEditTime
                    delay(200)

                    val searchFlag = synchronized(this) {
                        lastEditTime == now
                    }
                    if (searchFlag) {
                        searchWithKeyword(s.toString())
                    }
                }
            }
        })
    }

    private fun searchWithKeyword(keyword: String) {
        if (keyword.isNotEmpty()) {
            when (binding.searchViewpager.currentItem) {
                0 -> searchViewModel.searchNotice(keyword)
                1 -> searchViewModel.searchStaff(keyword)
            }
        } else {
            when (binding.searchViewpager.currentItem) {
                0 -> searchViewModel.clearNoticeList()
                1 -> searchViewModel.clearStaffList()
            }
        }
    }

    fun showAdviceText() {
        binding.searchAdviceTxt.visibility = View.VISIBLE
    }

    fun hideAdviceText() {
        binding.searchAdviceTxt.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.connectWebSocketIfDisconnected()
    }

    override fun onPause() {
        super.onPause()
        searchViewModel.disconnectWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.searchViewpager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}