package com.ku_stacks.ku_ring.ui.campus_onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ku_stacks.ku_ring.databinding.ActivityCampusOnboardingBinding
import timber.log.Timber

class CampusOnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCampusOnboardingBinding

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.indicatorView.selection = position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupFragment()
    }

    private fun setupBinding() {
        binding = ActivityCampusOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFragment() {
        binding.viewpager.apply {
            adapter = CampusOnBoardingPagerAdapter(supportFragmentManager, lifecycle)
        }

        binding.indicatorView.apply {
            count = 2
        }

        binding.viewpager.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}