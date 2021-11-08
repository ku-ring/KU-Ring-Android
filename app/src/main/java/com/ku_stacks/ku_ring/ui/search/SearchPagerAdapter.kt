package com.ku_stacks.ku_ring.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.ui.search.fragment.SearchNoticeFragment
import com.ku_stacks.ku_ring.ui.search.fragment.SearchStaffFragment

class SearchPagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm, lc) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchNoticeFragment()
            1 -> SearchStaffFragment()
            else -> error("no such position fragment in SearchActivity: $position")
        }
    }
}