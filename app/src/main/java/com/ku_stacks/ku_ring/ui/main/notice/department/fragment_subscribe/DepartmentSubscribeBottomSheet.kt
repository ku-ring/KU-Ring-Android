package com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentDepartmentSubscribeBottomSheetBinding

class DepartmentSubscribeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDepartmentSubscribeBottomSheetBinding

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

        initView()
    }

    private fun initView() {

    }

    companion object {
        fun newInstance(): DepartmentSubscribeBottomSheet {
            return DepartmentSubscribeBottomSheet()
        }
    }
}
