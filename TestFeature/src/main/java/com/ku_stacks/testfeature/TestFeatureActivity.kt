package com.ku_stacks.testfeature

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestFeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testfeature)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, TestFeatureActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
