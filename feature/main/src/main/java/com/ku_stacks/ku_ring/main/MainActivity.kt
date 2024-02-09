package com.ku_stacks.ku_ring.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.main.databinding.ActivityMainBinding
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.ui_util.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigator: KuringNavigator

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.mainBottomNavigation.selectedItemId = when (position) {
                0 -> R.id.notice_screen
                1 -> R.id.campus_screen
                2 -> R.id.setting_screen
                else -> throw IllegalStateException("no such main viewpager position")
            }
        }
    }

    private var backPressedTime = 0L

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        (intent?.getSerializableExtra(WebViewNotice.EXTRA_KEY) as? WebViewNotice)?.let { webViewNotice ->
            navToNoticeActivity(webViewNotice)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (intent?.getSerializableExtra(WebViewNotice.EXTRA_KEY) as? WebViewNotice)?.let { webViewNotice ->
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
            R.id.campus_screen -> binding.mainViewPager.setCurrentItem(1, false)
            R.id.setting_screen -> binding.mainViewPager.setCurrentItem(2, false)
            else -> return false
        }

        return true
    }

    private fun navToNoticeActivity(webViewNotice: WebViewNotice) {
        Timber.d("Notification received: $webViewNotice")
        navigator.navigateToNoticeWeb(this, webViewNotice)
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
                    WebViewNotice.EXTRA_KEY,
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
