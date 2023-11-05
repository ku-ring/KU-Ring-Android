package com.ku_stacks.ku_ring.ui.main.campus_onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ku_stacks.ku_ring.databinding.FragmentCampusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CampusFragment : Fragment() {

    private var _binding: FragmentCampusBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCampusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}