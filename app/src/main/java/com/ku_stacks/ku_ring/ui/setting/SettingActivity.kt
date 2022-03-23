package com.ku_stacks.ku_ring.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpBinding()
    }

    private fun setUpBinding() {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}