package com.ku_stacks.ku_ring.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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

        setContent {
            KuringTheme {
                LibrarySeatScreen(
                    onBackButtonClick = ::finish,
                    onReservationButtonClick = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LibrarySeatActivity::class.java)
            context.startActivity(intent)
        }
    }
}