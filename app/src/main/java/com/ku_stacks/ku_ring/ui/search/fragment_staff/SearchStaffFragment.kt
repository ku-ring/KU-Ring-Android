package com.ku_stacks.ku_ring.ui.search.fragment_staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentSearchStaffBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStaffFragment: Fragment() {

    private lateinit var binding: FragmentSearchStaffBinding
    private lateinit var staffAdapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_staff,container,false)
        binding.lifecycleOwner = activity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        staffAdapter = StaffAdapter()

        binding.searchStaffRecyclerview.layoutManager = LinearLayoutManager(activity)
        //binding.searchStaffRecyclerview.adapter = staffAdapter
    }
}