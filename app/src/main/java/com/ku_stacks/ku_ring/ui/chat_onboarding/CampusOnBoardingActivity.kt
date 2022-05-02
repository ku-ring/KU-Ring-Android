package com.ku_stacks.ku_ring.ui.chat_onboarding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.databinding.ActivityChatOnboardingBinding

class CampusOnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setupBinding()
        setupFragment()
    }

    private fun setupBinding() {
        binding = ActivityChatOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFragment() {
        val pagerAdapter = CampusOnBoardingPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = pagerAdapter
    }
}