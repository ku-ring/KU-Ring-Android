package com.ku_stacks.ku_ring.edit_departments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_departments.compose.EditDepartmentsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDepartmentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KuringTheme {
                EditDepartmentsScreen(
                    onClose = ::finish,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KuringTheme.colors.background),
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
            activity.apply {
                val intent = Intent(this, EditDepartmentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
            }
        }
    }
}