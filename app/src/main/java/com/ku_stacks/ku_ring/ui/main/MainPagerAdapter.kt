package com.ku_stacks.ku_ring.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.ui.main.campus_onboarding.CampusFragment
import com.ku_stacks.ku_ring.ui.main.notice.NoticeFragment
import com.ku_stacks.ku_ring.ui.main.search.SearchFragment
import com.ku_stacks.ku_ring.ui.main.setting.SettingFragment

class MainPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {
    private val items = arrayOf(
        { NoticeFragment() },
        { SearchFragment() },
        { CampusFragment() },
        { SettingFragment() }
    )

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment = items[position].invoke()
}