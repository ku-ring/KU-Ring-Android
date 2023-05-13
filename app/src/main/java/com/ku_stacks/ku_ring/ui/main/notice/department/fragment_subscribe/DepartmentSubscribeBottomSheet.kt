package com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.Department
import com.ku_stacks.ku_ring.databinding.FragmentDepartmentSubscribeBottomSheetBinding
import com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe.listener.DepartmentEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepartmentSubscribeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDepartmentSubscribeBottomSheetBinding

    private val mainViewModel: DepartmentSubscribeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            // TODO : 최대높이 동작하도록 수정
            behavior.peekHeight = resources.displayMetrics.heightPixels
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDepartmentSubscribeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
        initRecyclerView()
        observeViewModel()
    }

    private fun bindViewModel() {
        binding.viewModel = mainViewModel
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = DepartmentListAdapter(
                departmentEventListener = getDepartmentEventListener()
            )
        }
    }

    private fun getDepartmentEventListener(): DepartmentEventListener {
        return object : DepartmentEventListener {
            override fun onClickDepartment(uiData: Department) {
                // TODO : impl
            }
        }
    }

    private fun observeViewModel() {

    }

    companion object {
        fun newInstance(): DepartmentSubscribeBottomSheet {
            return DepartmentSubscribeBottomSheet()
        }
    }
}
