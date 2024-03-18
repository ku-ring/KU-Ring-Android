package com.ku_stacks.ku_ring.main.setting

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringThemeTest
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.databinding.FragmentSettingBinding
import com.ku_stacks.ku_ring.main.setting.compose.SettingScreen
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
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
    }

    private fun setupView() {
        val activity = requireActivity()

        binding.composeView.setContent {
            val isExtNotificationAllowed by viewModel.isExtNotificationAllowed.collectAsState()

            KuringThemeTest {
                SettingScreen(
                    onNavigateToEditSubscription = { navigator.navigateToEditSubscription(activity) },
                    isExtNotificationEnabled = isExtNotificationAllowed,
                    onExtNotificationEnabledToggle = viewModel::setExtNotificationAllowed,
                    onNavigateToUpdateLog = { startWebViewActivity(R.string.notion_new_contents_url) },
                    onNavigateToKuringTeam = { startWebViewActivity(R.string.notion_kuring_team_url) },
                    onNavigateToPrivacyPolicy = { startWebViewActivity(R.string.notion_privacy_policy_url) },
                    onNavigateToServiceTerms = { startWebViewActivity(R.string.notion_terms_of_service_url) },
                    onNavigateToOpenSources = {
                        navigator.navigateToOssLicensesMenu(activity)
                        activity.overridePendingTransition(
                            R.anim.anim_slide_right_enter,
                            R.anim.anim_stay_exit
                        )
                    },
                    onNavigateToKuringInstagram = ::navigateToKuringInstagram,
                    onNavigateToFeedback = { navigator.navigateToFeedback(activity) },
                    modifier = Modifier
                        .background(KuringTheme.colors.background)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
        }
    }

    private fun startWebViewActivity(@StringRes urlId: Int) {
        val url = getString(urlId)
        navigator.navigateToNotionView(requireActivity(), url)
        requireActivity().overridePendingTransition(
            R.anim.anim_slide_right_enter,
            R.anim.anim_stay_exit
        )
    }

    private fun navigateToKuringInstagram() {
        val intent = getInstagramIntent()
        startActivity(intent)
    }

    private fun getInstagramIntent(): Intent {
        val packageName = getString(R.string.instagram_package)
        val appScheme = getString(R.string.instagram_app_scheme)
        val webScheme = getString(R.string.instagram_web_scheme)
        return try {
            requireActivity().packageManager.getPackageInfo(packageName, 0)
            Intent(Intent.ACTION_VIEW, Uri.parse(appScheme))
        } catch (e: PackageManager.NameNotFoundException) {
            Intent(Intent.ACTION_VIEW, Uri.parse(webScheme))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
