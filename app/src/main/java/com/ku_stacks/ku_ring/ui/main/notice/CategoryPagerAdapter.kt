package com.ku_stacks.ku_ring.ui.main.notice

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.ui.main.notice.category.HomeBaseFragment

class CategoryPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {

    /**
     * array 의 내부를 lambda 로 하지않으면 memory leak 발생
     * (Fragment 가 destroy 될 때 참조가 남음)
     */
    private val items = listOf("bch", "sch", "emp", "nat", "stu", "ind", "nor", "lib")
        .map { shortCategory -> { HomeBaseFragment(shortCategory) } }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment = items[position].invoke()
}