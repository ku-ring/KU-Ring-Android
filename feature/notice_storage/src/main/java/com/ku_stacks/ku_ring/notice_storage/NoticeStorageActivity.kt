package com.ku_stacks.ku_ring.notice_storage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice_storage.compose.NoticeStorageScreen
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoticeStorageActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KuringTheme {
                NoticeStorageScreen(
                    onNoticeClick = ::startNoticeActivity,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    private fun startNoticeActivity(notice: Notice) {
        navigator.navigateToNoticeWeb(this, notice)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, NoticeStorageActivity::class.java)
            activity.startActivity(intent)
        }
    }
}