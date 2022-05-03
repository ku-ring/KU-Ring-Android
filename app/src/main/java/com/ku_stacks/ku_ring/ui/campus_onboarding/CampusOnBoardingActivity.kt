package com.ku_stacks.ku_ring.ui.campus_onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ku_stacks.ku_ring.databinding.ActivityCampusOnboardingBinding
import com.ku_stacks.ku_ring.ui.chat.ChatActivity
import com.ku_stacks.ku_ring.util.makeDialog

class CampusOnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCampusOnboardingBinding
    private val viewModel by viewModels<CampusOnBoardingViewModel>()
    private lateinit var pagerAdapter: CampusOnBoardingPagerAdapter

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.indicatorView.selection = position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupFragment()
        observeEvent()
    }

    private fun setupBinding() {
        binding = ActivityCampusOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFragment() {
        pagerAdapter = CampusOnBoardingPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(pageChangeCallback)
        }
    }

    private fun observeEvent() {
        viewModel.dialogEvent.observe(this) {
            makeDialog(description = getString(it))
        }

        viewModel.finishEvent.observe(this) {
            startChatActivity(it)
        }
    }

    private fun startChatActivity(nickname: String) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("nickname", nickname)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}