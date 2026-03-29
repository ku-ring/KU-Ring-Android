package com.ku_stacks.ku_ring.club.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent().setClassName(this, "com.ku_stacks.ku_ring.HostActivity")
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(context: Context, clubId: Int) {
            val intent = Intent(context, ClubDetailActivity::class.java).apply {
                putExtra(CLUB_ID_KEY, clubId)
            }
            context.startActivity(intent)
        }

        const val CLUB_ID_KEY = "CLUB_ID_KEY"
    }
}