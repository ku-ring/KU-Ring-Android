package com.ku_stacks.ku_ring.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.ui.search.fragment_notice.SearchNoticeFragment
import com.ku_stacks.ku_ring.ui.search.fragment_staff.SearchStaffFragment

class SearchPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {

    private val items = arrayOf(
        { SearchNoticeFragment() },
        { SearchStaffFragment() }
    )

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment = items[position].invoke()
}