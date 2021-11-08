package com.ku_stacks.ku_ring.ui.search

import android.os.Bundle
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
    private val viewModel by viewModels<SearchViewModel>()

    private val pageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Timber.e("pageSelect detected")
            when (position) {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupFragment()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}