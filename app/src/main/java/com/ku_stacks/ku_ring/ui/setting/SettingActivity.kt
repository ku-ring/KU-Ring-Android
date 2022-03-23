package com.ku_stacks.ku_ring.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySettingBinding
import com.ku_stacks.ku_ring.ui.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.ui.notion.NotionViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupView()
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

        binding.subscribeLayout.subscribeNoticeLayout.setOnClickListener {
            // TODO
        }
        binding.subscribeLayout.subscribeExtLayout.setOnClickListener {
            // TODO
        }

        binding.informationLayout.newContentsLayout.setOnClickListener {
            val intent = Intent(this, NotionViewActivity::class.java)
            intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_new_contents_url))
            startActivity(intent)
        }
        binding.informationLayout.teamLayout.setOnClickListener {
            val intent = Intent(this, NotionViewActivity::class.java)
            intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_kuring_team_url))
            startActivity(intent)
        }
        binding.informationLayout.privacyPolicyLayout.setOnClickListener {
            val intent = Intent(this, NotionViewActivity::class.java)
            intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_privacy_policy_url))
            startActivity(intent)
        }
        binding.informationLayout.termsOfServiceLayout.setOnClickListener {
            val intent = Intent(this, NotionViewActivity::class.java)
            intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.notion_terms_of_service_url))
            startActivity(intent)
        }
        binding.informationLayout.openSourceLayout.setOnClickListener {
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_license))
        }

        binding.snsLayout.instagramLayout.setOnClickListener {
            val intent = Intent(this, NotionViewActivity::class.java)
            intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.instagram_url))
            startActivity(intent)
        }
        binding.snsLayout.kakaoChannelLayout.setOnClickListener {
            val intent = Intent(this, NotionViewActivity::class.java)
            intent.putExtra(NotionViewActivity.NOTION_URL, getString(R.string.kakao_url))
            startActivity(intent)
        }

        binding.feedbackLayout.feedbackSendLayout.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}