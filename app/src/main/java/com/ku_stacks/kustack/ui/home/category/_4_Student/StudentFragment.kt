package com.ku_stacks.kustack.ui.home.category._4_Student

import android.os.Bundle
import android.view.View
import com.ku_stacks.kustack.ui.home.category.HomeCategoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentFragment : HomeCategoryFragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv1.text = "Student"
    }
}