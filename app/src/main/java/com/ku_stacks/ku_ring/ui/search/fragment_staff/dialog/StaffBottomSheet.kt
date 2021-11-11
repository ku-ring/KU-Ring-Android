package com.ku_stacks.ku_ring.ui.search.fragment_staff.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.websocket.response.SearchStaffResponse
import com.ku_stacks.ku_ring.databinding.DialogStaffBottomSheetBinding

class StaffBottomSheet: BottomSheetDialogFragment() {

    private var _binding : DialogStaffBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogStaffBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val staff = arguments?.getSerializable("staff") as SearchStaffResponse
        setupView(staff)
    }

    private fun setupView(staff: SearchStaffResponse?) {
        staff?.let {
            binding.staffBottomSheetNameTxt.text = staff.name
            binding.staffBottomSheetDepartmentTxt.text = staff.department +" · "+staff.college
            binding.staffBottomSheetEmailTxt.text = "✉ ${staff.email}"
            binding.staffBottomSheetLabTxt.text = "📍 ${staff.lab}"
            binding.staffBottomSheetPhoneTxt.text = "📞 ${staff.phone}"
            binding.staffBottomSheetMajorTxt.text = "📖 ${staff.major}"

            binding.staffBottomSheetEmailTxt.setOnClickListener {
                val clipboardManager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("email", staff.email)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireContext(), "이메일이 클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show()
            }

            binding.staffBottomSheetPhoneTxt.setOnClickListener {
                val clipboardManager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("phone number", staff.phone)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireContext(), "전화번호가 클립보드에 복사되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}