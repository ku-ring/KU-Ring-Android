package com.ku_stacks.ku_ring.initializer

import android.content.Context
import androidx.startup.Initializer
import com.ku_stacks.ku_ring.BuildConfig
import timber.log.Timber

class TimberInitializer: Initializer<Unit> {

    override fun create(context: Context){
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}