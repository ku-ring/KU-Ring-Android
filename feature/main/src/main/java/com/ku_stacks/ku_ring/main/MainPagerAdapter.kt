package com.ku_stacks.ku_ring.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.main.campus_onboarding.CampusFragment
import com.ku_stacks.ku_ring.main.notice.NoticesParentFragment
import com.ku_stacks.ku_ring.main.search.SearchFragment
import com.ku_stacks.ku_ring.main.setting.SettingFragment

class MainPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
    private val items = arrayOf(
        { NoticesParentFragment() },
        { SearchFragment() },
        { CampusFragment() },
        { SettingFragment() }
    )

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment = items[position].invoke()
}