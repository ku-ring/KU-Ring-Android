package com.ku_stacks.ku_ring.initializer

import android.content.Context
import androidx.startup.Initializer
import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.crashlytics.CrashlyticsTree
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        when (BuildConfig.BUILD_TYPE) {
            "debug" -> {
                Timber.plant(Timber.DebugTree())
            }
            "release" -> {
                Timber.plant(CrashlyticsTree())
            }
            else -> {
                Timber.plant(Timber.DebugTree())
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}