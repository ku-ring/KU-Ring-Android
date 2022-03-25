package com.ku_stacks.ku_ring.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySettingBinding
import com.ku_stacks.ku_ring.ui.edit_subscription.EditSubscriptionActivity
import com.ku_stacks.ku_ring.ui.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.ui.notion.NotionViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()

        binding.subscribeLayout.subscribeExtSwitch.isChecked = viewModel.isExtNotificationAllowed()
    }

    private fun setupBinding() {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupView() {
        binding.settingBackBt.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }

        /** subscribe layout */
        binding.subscribeLayout.subscribeNoticeLayout.setOnClickListener {
            val intent = Intent(this, EditSubscriptionActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
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
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license))
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
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
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }

    private fun startWebViewActivity(url: String) {
        val intent = Intent(this, NotionViewActivity::class.java)
        intent.putExtra(NotionViewActivity.NOTION_URL, url)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}