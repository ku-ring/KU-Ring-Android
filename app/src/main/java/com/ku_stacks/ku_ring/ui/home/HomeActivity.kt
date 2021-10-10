package com.ku_stacks.ku_ring.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var analytics : EventAnalytics

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

        getFcmToken()

        setupBinding()
        setupHeader()
        observeData()

        binding.homeText.setOnClickListener {
            Timber.e("homeText clicked")
            analytics.click("home btn", "HomeActivity")
            throw RuntimeException("Test Crash")
        }
    }

    private fun setupBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupHeader(){
        binding.homeViewpager.adapter = HomePagerAdapter(supportFragmentManager,lifecycle)
        binding.homeViewpager.registerOnPageChangeCallback(pageChangeCallback)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, binding.homeViewpager,true) { tab, position ->
            //여기서 등록한 푸시알림으로 색깔 변경도 가능할듯?
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

        val materialToolbar = findViewById<MaterialToolbar>(R.id.material_toolbar)
        materialToolbar.setNavigationOnClickListener {
            Snackbar.make(binding.root, "menu clicked", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeData(){

        viewModel.homeTabState.observe(this){
            binding.homeText.text = "${it.name} in HomeActivity"
            Timber.e("${it.name} observed")
        }
    }

    private fun getFcmToken() {
        CoroutineScope(Dispatchers.Default).launch {
            val instance = FirebaseInstallations.getInstance()
            Timber.e("FCM instance : $instance")

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    Timber.e("Firebase instanceId fail : ${task.exception}")
                    return@addOnCompleteListener
                }
                val token = task.result
                Timber.e("FCM token : $token")
            }
        }

    }
}