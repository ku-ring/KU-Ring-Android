package com.ku_stacks.ku_ring.ui.home.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentHomeCategoryBinding

abstract class HomeCategoryFragment : Fragment(){

    lateinit var binding: FragmentHomeCategoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_category,container,false)
        binding.lifecycleOwner = activity

        return binding.root
    }
}