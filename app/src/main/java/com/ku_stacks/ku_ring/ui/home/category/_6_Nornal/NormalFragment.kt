package com.ku_stacks.ku_ring.ui.home.category._6_Nornal

import android.os.Bundle
import android.view.View
import com.ku_stacks.ku_ring.ui.home.category.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NormalFragment : HomeBaseFragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.text = "Normal"
    }
}