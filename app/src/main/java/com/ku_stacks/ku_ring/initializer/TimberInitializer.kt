package com.ku_stacks.ku_ring.initializer

import android.content.Context
import androidx.startup.Initializer
import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.thirdparty.firebase.crashlytics.CrashlyticsTree
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val tree = when (BuildConfig.BUILD_TYPE) {
            "debug" -> Timber.DebugTree()
            "release" -> CrashlyticsTree()
            else -> Timber.DebugTree()
        }
        Timber.plant(tree)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}