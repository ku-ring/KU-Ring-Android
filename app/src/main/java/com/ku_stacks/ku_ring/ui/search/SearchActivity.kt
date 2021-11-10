package com.ku_stacks.ku_ring.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    private var currentPage = noticeSearchPage

    private val pageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Timber.e("pageSelect detected")
            when (position) {
                0 -> currentPage = noticeSearchPage
                1 -> currentPage = staffSearchPage
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupFragment()
        setupView()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.viewModel = searchViewModel
    }

    private fun setupFragment() {
        val pagerAdapter = SearchPagerAdapter(supportFragmentManager, lifecycle)
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
        binding.searchBackBt.setOnClickListener {
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
            finish()
        }

        binding.searchKeywordEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()) {
                    binding.searchAdviceTxt.visibility = View.GONE
                    when(currentPage) {
                        noticeSearchPage -> {
                            searchViewModel.searchNotice(s.toString())
                        }
                        staffSearchPage -> {
                            searchViewModel.searchStaff(s.toString())
                        }
                    }
                } else {
                    binding.searchAdviceTxt.visibility = View.VISIBLE
                    when(currentPage) {
                        noticeSearchPage -> {
                            searchViewModel.clearNoticeList()
                        }
                        staffSearchPage -> {
                            searchViewModel.clearStaffList()
                        }
                    }
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.connectWebSocketIfDisconnected()
    }

    override fun onStop() {
        super.onStop()
        searchViewModel.disconnectWebSocket()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val noticeSearchPage = 0
        const val staffSearchPage = 1
    }
}