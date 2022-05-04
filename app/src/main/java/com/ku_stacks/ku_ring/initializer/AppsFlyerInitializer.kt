package com.ku_stacks.ku_ring.initializer

import android.content.Context
import androidx.startup.Initializer
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.ku_stacks.ku_ring.BuildConfig
import timber.log.Timber

class AppsFlyerInitializer: Initializer<Unit> {

    private val devKey = BuildConfig.APPS_FLYER_DEV_KEY

    override fun create(context: Context) {
        AppsFlyerLib.getInstance().apply {
            if (BuildConfig.DEBUG) {
                setDebugLog(true)
            }

            init(devKey, null, context)
            start(context, devKey, object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Timber.e("AppsFlyer start success")
                }

                override fun onError(errorCode: Int, errorDesc: String) {
                    Timber.e("AppsFlyer start error : [$errorCode] $errorDesc")
                }
            })
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}