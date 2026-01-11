package com.ku_stacks.ku_ring.main.archive

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R.anim.anim_slide_left_enter
import com.ku_stacks.ku_ring.main.R.anim.anim_slide_left_exit
import com.ku_stacks.ku_ring.main.R.anim.anim_slide_right_enter
import com.ku_stacks.ku_ring.main.R.anim.anim_stay_exit
import com.ku_stacks.ku_ring.main.archive.compose.ArchiveScreen
import com.ku_stacks.ku_ring.compose_locals.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.util.TransitionType
import com.ku_stacks.ku_ring.util.setActivityTransition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setContent {
            KuringCompositionLocalProvider {
                KuringTheme {
                    ArchiveScreen()
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        setActivityTransition(
            TransitionType.CLOSE,
            anim_slide_left_enter,
            anim_slide_left_exit
        )
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ArchiveActivity::class.java)
            activity.startActivity(intent)
            activity.setActivityTransition(
                TransitionType.OPEN,
                anim_slide_right_enter,
                anim_stay_exit
            )
        }
    }
}
