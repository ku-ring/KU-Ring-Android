package com.ku_stacks.ku_ring.club.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.util.navigateToExternalBrowser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KuringTheme {
                val context = LocalContext.current

                ClubDetailScreen(
                    onBack = ::finish,
                    onMoveToRecruitmentLink = context::navigateToExternalBrowser,
                    modifier = Modifier
                        .background(KuringTheme.colors.background)
                        .safeDrawingPadding()
                        .fillMaxSize(),
                )
            }
        }
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