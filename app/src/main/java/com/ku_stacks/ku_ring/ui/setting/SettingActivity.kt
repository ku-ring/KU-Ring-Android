package com.ku_stacks.ku_ring.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.databinding.ActivitySettingBinding
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }
}