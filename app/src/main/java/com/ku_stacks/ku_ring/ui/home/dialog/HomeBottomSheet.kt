package com.ku_stacks.ku_ring.ui.home.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ku_stacks.ku_ring.databinding.DialogHomeMoreBinding
import timber.log.Timber

enum class NextActivityItem{
    Feedback, OpenSource, PersonalInfo
}

class HomeBottomSheet(val itemClick: (NextActivityItem) -> (Unit)) : BottomSheetDialogFragment() {
    private var _binding: DialogHomeMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogHomeMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.feedbackLayout.setOnClickListener {
            itemClick(NextActivityItem.Feedback)
            dialog?.dismiss()
        }
        binding.openSourceLayout.setOnClickListener {
            itemClick(NextActivityItem.OpenSource)
            dialog?.dismiss()
        }
        binding.personalDataLayout.setOnClickListener {
            itemClick(NextActivityItem.PersonalInfo)
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Timber.e("HomeBottomSheet destroyed")
    }
}