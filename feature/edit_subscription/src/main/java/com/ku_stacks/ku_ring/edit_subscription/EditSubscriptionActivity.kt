package com.ku_stacks.ku_ring.edit_subscription

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.edit_subscription.compose.EditSubscriptionScreen
import com.ku_stacks.ku_ring.thirdparty.compose.KuringCompositionLocalProvider
import com.ku_stacks.ku_ring.thirdparty.di.LocalNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSubscriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KuringCompositionLocalProvider {
                val navigator = LocalNavigator.current
                KuringTheme {
                    EditSubscriptionScreen(
                        onNavigateToBack = ::finish,
                        onAddDepartmentButtonClick = {
                            navigator.navigateToEditSubscribedDepartment(this)
                        },
                        onFinish = {
                            setResult(RESULT_OK)
                            finish()
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
    }

    companion object {
        const val FIRST_RUN_FLAG = "firstRunFlag"
        fun start(activity: Activity, isFirstRun: Boolean) {
            val intent = Intent(activity, EditSubscriptionActivity::class.java).apply {
                putExtra(FIRST_RUN_FLAG, isFirstRun)
            }
            activity.startActivity(intent)
        }
    }
}