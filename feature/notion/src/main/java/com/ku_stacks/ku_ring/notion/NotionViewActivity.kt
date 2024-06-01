package com.ku_stacks.ku_ring.notion

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

class NotionViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(NOTION_URL)
        setContent {
            KuringTheme {
                NotionScreen(
                    url = url,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val NOTION_URL = "notion_url"
        fun start(activity: Activity, notionUrl: String) {
            val intent = Intent(activity, NotionViewActivity::class.java).apply {
                putExtra(NOTION_URL, notionUrl)
            }
            activity.startActivity(intent)
        }
    }
}