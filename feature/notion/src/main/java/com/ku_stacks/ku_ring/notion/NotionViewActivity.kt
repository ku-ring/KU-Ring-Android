package com.ku_stacks.ku_ring.notion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotionViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent().setClassName(this, "com.ku_stacks.ku_ring.HostActivity")
        startActivity(intent)
        finish()
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