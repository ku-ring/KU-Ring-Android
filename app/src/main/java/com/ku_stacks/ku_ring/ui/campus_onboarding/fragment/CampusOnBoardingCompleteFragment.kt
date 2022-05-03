package com.ku_stacks.ku_ring.ui.campus_onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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