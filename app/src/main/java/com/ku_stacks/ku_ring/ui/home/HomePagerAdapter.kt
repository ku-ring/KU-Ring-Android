package com.ku_stacks.ku_ring.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ku_stacks.ku_ring.ui.home.category.EmployFragment
import com.ku_stacks.ku_ring.ui.home.category.ScholarshipFragment
import com.ku_stacks.ku_ring.ui.home.category._0_Bachelor.BachelorFragment
import com.ku_stacks.ku_ring.ui.home.category._3_nation.NationFragment
import com.ku_stacks.ku_ring.ui.home.category._4_Student.StudentFragment
import com.ku_stacks.ku_ring.ui.home.category._5_Industry.IndustryFragment
import com.ku_stacks.ku_ring.ui.home.category._6_Nornal.NormalFragment
import com.ku_stacks.ku_ring.ui.home.category._7_Library.LibraryFragment

class HomePagerAdapter(fm: FragmentManager, lc: Lifecycle): FragmentStateAdapter(fm, lc) {
    override fun getItemCount() = 8
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BachelorFragment()
            1 -> ScholarshipFragment()
            2 -> EmployFragment()
            3 -> NationFragment()
            4 -> StudentFragment()
            5 -> IndustryFragment()
            6 -> NormalFragment()
            7 -> LibraryFragment()
            else -> error("no such position: $position")
        }
    }
}