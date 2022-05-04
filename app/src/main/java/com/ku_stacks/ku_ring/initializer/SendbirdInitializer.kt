package com.ku_stacks.ku_ring.initializer

import android.content.Context
import androidx.startup.Initializer
import com.ku_stacks.ku_ring.BuildConfig
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.android.params.InitParams
import timber.log.Timber

class SendbirdInitializer : Initializer<Unit> {

    private val appId = BuildConfig.SENDBIRD_APP_ID

    override fun create(context: Context) {
        SendbirdChat.init(
            InitParams(
                appId = appId,
                context = context.applicationContext,
                useCaching = false
            ),
            object : InitResultHandler {
                override fun onMigrationStarted() {
                    // This won't be called if useLocalCaching is set to false.
                    Timber.e("Sendbird migration started")
                }

                override fun onInitSucceed() {
                    // This won't be called if useLocalCaching is set to false.
                    Timber.e("Sendbird init succeed")
                }

                override fun onInitFailed(e: SendbirdException) {
                    Timber.e("Sendbird init failed : [${e.code}] ${e.message}")
                }
            }
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}