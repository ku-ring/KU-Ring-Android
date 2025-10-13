package com.ku_stacks.ku_ring.kuringbot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.kuringbot.compose.KuringBotScreen
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KuringBotActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KuringTheme {
                KuringBotScreen(
                    onBackButtonClick = ::finish,
                    onMoveToLogin = { navigator.navigateToAuth(this) },
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize(),
                )
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, KuringBotActivity::class.java)
            context.startActivity(intent)
        }
    }

}