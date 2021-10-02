package com.ku_stacks.kustack.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ku_stacks.kustack.R
import com.ku_stacks.kustack.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    private val pageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Timber.e("pageSelect detected")
            when(position){
                0 -> viewModel.onBchTabClick()
                1 -> viewModel.onSchTabClick()
                2 -> viewModel.onEmpTabClick()
                3 -> viewModel.onNatTabClick()
                4 -> viewModel.onStuTabClick()
                5 -> viewModel.onIndTabClick()
                6 -> viewModel.onNorTabClick()
                7 -> viewModel.onLibTabClick()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpView()
        setUpViewModel()

    }

    private fun setUpView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.homeViewpager.adapter = HomePagerAdapter(supportFragmentManager,lifecycle)
        binding.homeViewpager.registerOnPageChangeCallback(pageChangeCallback)

        val tabLayout = binding.homeHeader.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, binding.homeViewpager,true) { tab, position ->
            when(position){
                0 -> tab.text = "학사"
                1 -> tab.text = "장학"
                2 -> tab.text = "취창업"
                3 -> tab.text = "국제"
                4 -> tab.text = "학생"
                5 -> tab.text = "산학"
                6 -> tab.text = "일반"
                7 -> tab.text = "도서관"
            }
        }.attach()
    }

    private fun setUpViewModel(){

        viewModel.homeTabState.observe(this){
            binding.homeText.text = "${it.name} in HomeActivity"
            Timber.e("${it.name} observed")

        }
    }
}