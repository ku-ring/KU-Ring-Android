package com.ku_stacks.ku_ring.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.library.compose.LibrarySeatScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibrarySeatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {
                LibrarySeatScreen(
                    onNavigateBack = ::finish,
                    onLaunchLibraryIntent = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }



    companion object {
        fun start(activity: Activity) {
            with(activity) {
                val intent = Intent(this, LibrarySeatActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.anim_slide_right_enter,
                    R.anim.anim_stay_exit
                )
            }
        }
    }
}