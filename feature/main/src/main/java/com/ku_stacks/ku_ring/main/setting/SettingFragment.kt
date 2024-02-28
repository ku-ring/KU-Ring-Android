package com.ku_stacks.ku_ring.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.FragmentSettingBinding
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.ui_util.getAppVersionName
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    @Inject
    lateinit var navigator: KuringNavigator

    private var _binding: FragmentSettingBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        binding.subscribeLayout.subscribeExtSwitch.isChecked = viewModel.isExtNotificationAllowed()
    }

    private fun setupView() {
        /** subscribe layout */
        binding.subscribeLayout.subscribeNoticeLayout.setOnClickListener {
            navigator.navigateToEditSubscription(requireActivity())
            requireActivity().overridePendingTransition(
                R.anim.anim_slide_right_enter,
                R.anim.anim_stay_exit
            )
        }
        binding.subscribeLayout.subscribeExtSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setExtNotificationAllowed(isChecked)
        }

        /** information layout */
        binding.informationLayout.newContentsLayout.setOnClickListener {
            startWebViewActivity(getString(R.string.notion_new_contents_url))
        }
        binding.informationLayout.teamLayout.setOnClickListener {
            startWebViewActivity(getString(R.string.notion_kuring_team_url))
        }
        binding.informationLayout.privacyPolicyLayout.setOnClickListener {
            startWebViewActivity(getString(R.string.notion_privacy_policy_url))
        }
        binding.informationLayout.termsOfServiceLayout.setOnClickListener {
            startWebViewActivity(getString(R.string.notion_terms_of_service_url))
        }
        binding.informationLayout.openSourceLayout.setOnClickListener {
            val activity = requireActivity()
            navigator.navigateToOssLicensesMenu(activity)
            activity.overridePendingTransition(
                R.anim.anim_slide_right_enter,
                R.anim.anim_stay_exit
            )
        }

        /** feedback layout */
        binding.feedbackLayout.feedbackSendLayout.setOnClickListener {
            navigator.navigateToFeedback(requireActivity())
            requireActivity().overridePendingTransition(
                R.anim.anim_slide_right_enter,
                R.anim.anim_stay_exit
            )
        }

        /** set app version name */
        binding.informationLayout.versionName = requireContext().getAppVersionName()
    }

    private fun startWebViewActivity(url: String) {
        navigator.navigateToNotionView(requireActivity(), url)
        requireActivity().overridePendingTransition(
            R.anim.anim_slide_right_enter,
            R.anim.anim_stay_exit
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}