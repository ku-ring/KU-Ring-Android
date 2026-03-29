package com.ku_stacks.ku_ring.edit_subscription

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSubscriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent().setClassName(this, "com.ku_stacks.ku_ring.HostActivity")
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, EditSubscriptionActivity::class.java)
            activity.startActivity(intent)
        }
    }
}