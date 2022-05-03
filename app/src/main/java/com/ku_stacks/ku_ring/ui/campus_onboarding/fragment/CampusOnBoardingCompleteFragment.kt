package com.ku_stacks.ku_ring.ui.campus_onboarding.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.adapter.disableStartButtonIf
import com.ku_stacks.ku_ring.databinding.FragmentCampusOnboardingCompleteBinding
import com.ku_stacks.ku_ring.ui.campus_onboarding.CampusOnBoardingViewModel
import timber.log.Timber

class CampusOnBoardingCompleteFragment : Fragment() {

    private var _binding: FragmentCampusOnboardingCompleteBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by activityViewModels<CampusOnBoardingViewModel>()

    init {
        Timber.e("CampusCompleteFragment init")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampusOnboardingCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding.nicknameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                val isValidFormat = viewModel.isValidNicknameFormat(s.toString())
                if (isValidFormat) {
                    binding.nicknameFormatDescTv.text = getString(R.string.nickname_valid_format)
                    binding.nicknameFormatDescTv.setTextColor(getColor(requireContext(), R.color.kus_gray))
                } else {
                    binding.nicknameFormatDescTv.text = getString(R.string.nickname_not_valid_format)
                    binding.nicknameFormatDescTv.setTextColor(getColor(requireContext(), R.color.kus_pink))
                }
            }
        })

        binding.startBt.setOnClickListener {
            val nickname = binding.nicknameEt.text.toString()
            viewModel.authorizeNickname(nickname)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}