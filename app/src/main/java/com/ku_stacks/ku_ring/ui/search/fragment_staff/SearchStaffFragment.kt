package com.ku_stacks.ku_ring.ui.search.fragment_staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.entity.Staff
import com.ku_stacks.ku_ring.databinding.FragmentSearchStaffBinding
import com.ku_stacks.ku_ring.ui.search.SearchActivity
import com.ku_stacks.ku_ring.ui.search.SearchViewModel
import com.ku_stacks.ku_ring.ui.search.fragment_staff.dialog.StaffBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStaffFragment: Fragment() {

    private lateinit var binding: FragmentSearchStaffBinding
    private val searchViewModel by activityViewModels<SearchViewModel>()
    private lateinit var searchStaffAdapter: SearchStaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_staff, container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()
        observeData()
    }

    private fun setupListAdapter() {
        searchStaffAdapter = SearchStaffAdapter {
            invokeStaffInfoDialog(it)
        }
        binding.searchStaffRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.searchStaffRecyclerview.adapter = searchStaffAdapter
    }

    private fun observeData() {
        searchViewModel.staffList.observe(viewLifecycleOwner) {
            searchStaffAdapter.submitList(it)
            if (it.isEmpty()) {
                (activity as SearchActivity).showAdviceText()
            } else {
                (activity as SearchActivity).hideAdviceText()
            }
        }
    }

    private fun invokeStaffInfoDialog(staff: Staff) {
        val bottomSheet = StaffBottomSheet()
        val bundle = Bundle()
        bundle.putSerializable("staff", staff)
        bottomSheet.arguments = bundle
        bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
    }
}