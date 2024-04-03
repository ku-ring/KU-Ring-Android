package com.ku_stacks.ku_ring.main.setting.compose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.inner_screen.OpenSourceScreen
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: compose 완전 migration 및 메인 화면 navigation 통합 후 삭제 예정
@AndroidEntryPoint
class OpenSourceActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KuringTheme {
                OpenSourceScreen(
                    closeOpenSourceScreen = ::finish,
                    navigateToOssLicenses = {
                        navigator.navigateToOssLicensesMenu(this)
                        overridePendingTransition(
                            R.anim.anim_slide_right_enter,
                            R.anim.anim_stay_exit
                        )
                    },
                    navigateToLottieFiles = { startWebViewActivity(R.string.lottie_files) },
                    navigateToTossFaceCopyright = { startWebViewActivity(R.string.toss_face_copyright) },
                    modifier = Modifier
                        .background(KuringTheme.colors.background)
                        .fillMaxSize(),
                )
            }
        }
    }

    private fun startWebViewActivity(@StringRes urlId: Int) {
        val url = getString(urlId)
        navigator.navigateToNotionView(this, url)
        overridePendingTransition(
            R.anim.anim_slide_right_enter,
            R.anim.anim_stay_exit
        )
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, OpenSourceActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }
}