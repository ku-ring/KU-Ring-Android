package com.ku_stacks.ku_ring.edit_departments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDepartmentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent().setClassName(this, "com.ku_stacks.ku_ring.HostActivity")
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, EditDepartmentsActivity::class.java))
        }
    }
}
