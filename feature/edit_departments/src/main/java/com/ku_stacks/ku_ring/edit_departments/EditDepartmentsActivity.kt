package com.ku_stacks.ku_ring.edit_departments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_departments.compose.EditDepartments
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDepartmentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KuringTheme {
                EditDepartments(
                    onClose = ::finish,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    // TODO: companion object에 start 함수 구현
}