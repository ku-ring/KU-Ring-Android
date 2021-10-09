package com.ku_stacks.ku_ring.ui.home.category._3_nation

import android.os.Bundle
import android.view.View
import com.ku_stacks.ku_ring.ui.home.category.HomeCategoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NationFragment : HomeCategoryFragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.text = "Nation"
    }
}