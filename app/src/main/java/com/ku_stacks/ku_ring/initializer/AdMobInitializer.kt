package com.ku_stacks.ku_ring.initializer

import android.content.Context
import androidx.startup.Initializer
import com.google.android.gms.ads.MobileAds
import timber.log.Timber

class AdMobInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        MobileAds.initialize(context) { initializerStatus ->
            Timber.e("AdMob initializerStatus : $initializerStatus")
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}