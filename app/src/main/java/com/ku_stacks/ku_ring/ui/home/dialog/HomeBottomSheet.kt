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

class HomeBottomSheet: BottomSheetDialogFragment() {
    private var _binding: DialogHomeMoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemClick: (NextActivityItem) -> (Unit)

    /*
    Fragment는 기본생성자로 생성해야함.
    그렇지 않고 생성자에서 인자를 주면 화면회전 시 반드시 Unable to instantiate fragment...의 에러가 발생!
    따라서 아래와 같이 람다식을 인자로 click listener 를 넘긴다.
     */
    fun setArgument(click: (NextActivityItem) -> (Unit)){
        itemClick = click
    }

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