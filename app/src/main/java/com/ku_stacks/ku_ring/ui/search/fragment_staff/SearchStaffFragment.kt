package com.ku_stacks.ku_ring.ui.search.fragment_staff

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentSearchStaffBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStaffFragment: Fragment() {

    private lateinit var binding: FragmentSearchStaffBinding
    private val viewModel by viewModels<SearchStaffViewModel>()
    private lateinit var staffAdapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_staff, container, false)
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupView()
        observeData()
    }

    private fun setupAdapter() {
        staffAdapter = StaffAdapter()
        binding.searchStaffRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.searchStaffRecyclerview.adapter = staffAdapter
    }

    private fun setupView() {
        activity?.findViewById<EditText>(R.id.search_keyword_et)
            ?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
                override fun afterTextChanged(s: Editable?) {
                    viewModel.searchStaff(s.toString())
                }
            })
    }

    private fun observeData() {
        viewModel.staffList.observe(viewLifecycleOwner) {
            staffAdapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        //initWebSocket
    }

    override fun onStop() {
        super.onStop()
        //disconnect
    }
}