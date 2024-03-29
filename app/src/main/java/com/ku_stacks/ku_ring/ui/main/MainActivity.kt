package com.ku_stacks.ku_ring.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.data.model.WebViewNotice
import com.ku_stacks.ku_ring.databinding.ActivityMainBinding
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import com.ku_stacks.ku_ring.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.mainBottomNavigation.selectedItemId = when (position) {
                0 -> R.id.notice_screen
                1 -> R.id.search_screen
                2 -> R.id.campus_screen
                3 -> R.id.setting_screen
                else -> throw IllegalStateException("no such main viewpager position")
            }
        }
    }

    private var backPressedTime = 0L

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        (intent?.getSerializableExtra(NoticeWebActivity.WEB_VIEW_NOTICE) as? WebViewNotice)?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (intent?.getSerializableExtra(NoticeWebActivity.WEB_VIEW_NOTICE) as? WebViewNotice)?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }

        setupBinding()
        setupView()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupView() {
        val pagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)

        binding.mainViewPager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(pageChangeCallback)
            isUserInputEnabled = false
            offscreenPageLimit = pagerAdapter.itemCount
        }

        binding.mainBottomNavigation.setOnItemSelectedListener {
            navigationSelected(it)
        }
    }

    private fun navigationSelected(item: MenuItem): Boolean {
        when (item.setChecked(true).itemId) {
            R.id.notice_screen -> binding.mainViewPager.setCurrentItem(0, false)
            R.id.search_screen -> binding.mainViewPager.setCurrentItem(1, false)
            R.id.campus_screen -> binding.mainViewPager.setCurrentItem(2, false)
            R.id.setting_screen -> binding.mainViewPager.setCurrentItem(3, false)
            else -> return false
        }

        return true
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        Timber.d("Notification received: $webViewNotice")
        NoticeWebActivity.start(this, webViewNotice)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
        } else {
            showToast(getString(R.string.home_finish_if_back_again))
            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mainViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)

        fun start(
            activity: Activity,
            url: String,
            articleId: String,
            category: String,
        ) {
            val intent = createIntent(activity).apply {
                putExtra(
                    NoticeWebActivity.WEB_VIEW_NOTICE,
                    WebViewNotice(url, articleId, category),
                )
            }
            activity.startActivity(intent)
        }

        fun start(activity: Activity) {
            val intent = createIntent(activity)
            activity.startActivity(intent)
        }
    }
}
