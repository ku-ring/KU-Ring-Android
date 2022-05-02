package com.ku_stacks.ku_ring.ui.chat_onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.ui.chat_onboarding.fragment.CampusOnBoardingCompleteFragment
import com.ku_stacks.ku_ring.ui.chat_onboarding.fragment.CampusOnBoardingStartFragment

class CampusOnBoardingPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {

    private val items = arrayOf(
        { CampusOnBoardingStartFragment() },
        { CampusOnBoardingCompleteFragment() }
    )

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment = items[position].invoke()
}