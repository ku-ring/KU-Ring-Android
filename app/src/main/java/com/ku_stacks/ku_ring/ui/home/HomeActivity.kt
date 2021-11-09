package com.ku_stacks.ku_ring.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.databinding.ActivityHomeBinding
import com.ku_stacks.ku_ring.ui.detail.DetailActivity
import com.ku_stacks.ku_ring.ui.feedback.FeedbackActivity
import com.ku_stacks.ku_ring.ui.home.dialog.HomeBottomSheet
import com.ku_stacks.ku_ring.ui.home.dialog.NextActivityItem
import com.ku_stacks.ku_ring.ui.my_notification.NotificationActivity
import com.ku_stacks.ku_ring.ui.personal_info.PersonalDataActivity
import com.ku_stacks.ku_ring.ui.search.SearchActivity
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var analytics : EventAnalytics

    @Inject
    lateinit var pref: PreferenceUtil

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

        setupBinding()
        setupHeader()
        observeData()

        getFcmToken()

    /*
        anlytics, crashlytics 예시
        binding.homeText.setOnClickListener {
        Timber.e("homeText clicked")
        analytics.click("home btn", "HomeActivity")
        //throw RuntimeException("Crash On Release ver")
        }
    */

    }

    private fun setupBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupHeader(){
        val pagerAdapter = HomePagerAdapter(supportFragmentManager, lifecycle)
        binding.homeViewpager.adapter = pagerAdapter
        binding.homeViewpager.registerOnPageChangeCallback(pageChangeCallback)
        //binding.homeViewpager.offscreenPageLimit = pagerAdapter.itemCount

        TabLayoutMediator(binding.homeHeader.tabLayout, binding.homeViewpager,true) { tab, position ->
            //여기서 등록한 푸시알림으로 색깔 변경도 가능할듯?
            when (position) {
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

        binding.homeHeader.bellImg.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }

        binding.homeHeader.menuImg.setOnClickListener {
            invokeMenuDialog()
        }

        binding.homeHeader.searchImg.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }

    private fun observeData(){
        viewModel.homeTabState.observe(this) {
            Timber.e("${it.name} observed")
        }

        viewModel.pushCount.observe(this) {
            when (it)  {
                0 -> {
                    binding.homeHeader.notiCountBt.visibility = View.GONE
                }
                in 1..99 -> {
                    binding.homeHeader.notiCountBt.visibility = View.VISIBLE
                    binding.homeHeader.notiCountBt.text = it.toString()
                }
                else -> {
                    binding.homeHeader.notiCountBt.visibility = View.VISIBLE
                    binding.homeHeader.notiCountBt.text = getString(R.string.push_notification_max_count)
                }
            }
        }
    }

    private fun getFcmToken() {
        lifecycleScope.launch {
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
    private fun invokeMenuDialog() {
        val bottomSheet = HomeBottomSheet()
        bottomSheet.setArgument {
            when (it) {
                NextActivityItem.Feedback -> {
                    analytics.click("bottomSheet_Feedback btn", "HomeActivity")
                    val intent = Intent(this, FeedbackActivity::class.java)
                    startActivity(intent)
                }
                NextActivityItem.OpenSource -> {
                    analytics.click("bottomSheet_OpenSource btn", "HomeActivity")
                    startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                    OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")

                }
                NextActivityItem.PersonalInfo -> {
                    analytics.click("bottomSheet_PersonalInformation btn", "HomeActivity")
                    val intent = Intent(this, PersonalDataActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    fun updateNoticeTobeRead(notice: Notice) {
        viewModel.updateNoticeTobeRead(notice)
    }

    fun insertNotice(articleId: String, category: String) {
        viewModel.insertNotice(articleId, category)
    }
}