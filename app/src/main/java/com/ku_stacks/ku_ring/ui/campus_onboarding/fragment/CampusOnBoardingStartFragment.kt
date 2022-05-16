package com.ku_stacks.ku_ring.ui.campus_onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ku_stacks.ku_ring.databinding.FragmentCampusOnboardingStartBinding
import timber.log.Timber

class CampusOnBoardingStartFragment : Fragment() {

    private var _binding: FragmentCampusOnboardingStartBinding? = null
    private val binding
        get() = _binding!!

    init {
        Timber.e("CampusStartFragment init")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampusOnboardingStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}