package com.ku_stacks.ku_ring.ui.main.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.FragmentSettingBinding
import com.ku_stacks.ku_ring.ui.edit_subscription.EditSubscriptionActivity
import com.ku_stacks.ku_ring.ui.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.ui.notion.NotionViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

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
            val intent = Intent(requireContext(), EditSubscriptionActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
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
            val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
            startActivity(intent)
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license))
            requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }

        /** sns layout */
        binding.snsLayout.instagramLayout.setOnClickListener {
            startWebViewActivity(getString(R.string.instagram_url))
        }
        binding.snsLayout.kakaoChannelLayout.setOnClickListener {
            startWebViewActivity(getString(R.string.kakao_url))
        }

        /** feedback layout */
        binding.feedbackLayout.feedbackSendLayout.setOnClickListener {
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }

    private fun startWebViewActivity(url: String) {
        val intent = Intent(requireContext(), NotionViewActivity::class.java)
        intent.putExtra(NotionViewActivity.NOTION_URL, url)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}