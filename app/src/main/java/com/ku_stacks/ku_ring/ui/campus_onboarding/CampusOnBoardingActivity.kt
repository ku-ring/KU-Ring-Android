package com.ku_stacks.ku_ring.ui.campus_onboarding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.databinding.ActivityCampusOnboardingBinding

class CampusOnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCampusOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setupBinding()
        setupFragment()
    }

    private fun setupBinding() {
        binding = ActivityCampusOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFragment() {
        val pagerAdapter = CampusOnBoardingPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = pagerAdapter
    }
}