package com.ku_stacks.ku_ring.ui.home.category._5_Industry

import android.os.Bundle
import android.view.View
import com.ku_stacks.ku_ring.ui.home.category.HomeCategoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndustryFragment : HomeCategoryFragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.text = "Industry"
    }
}