package com.ku_stacks.ku_ring.main.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.search.compose.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
    }

    private fun setupView() {
        setContent {
            KuringTheme {
                SearchScreen(
                    viewModel = viewModel,
                    onNavigationClick = { finish() },
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
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
        }
    }
}
