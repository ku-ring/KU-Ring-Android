package com.ku_stacks.ku_ring.library

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.library.compose.LibrarySeatScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LibrarySeatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuringTheme {
                LibrarySeatScreen(
                    onNavigateBack = ::finish,
                    onLaunchLibraryIntent = ::launchLibrary,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                R.anim.anim_slide_left_enter,
                R.anim.anim_slide_left_exit
            )
        } else {
            overridePendingTransition(R.anim.anim_slide_left_enter, R.anim.anim_slide_left_exit)
        }
    }

    private fun launchLibrary() {
        try {
            checkKonkukLibraryInstalled()
            launchKonkukLibrary()
        } catch (e: PackageManager.NameNotFoundException) {
            launchPlayStore()
        }
    }

    private fun checkKonkukLibraryInstalled() {
        try {
            val packageManager = this.packageManager
            packageManager.getPackageInfo(
                KU_LIBRARY_PACKAGE_NAME,
                PackageManager.MATCH_UNINSTALLED_PACKAGES
            )
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("PackageManager could not find $KU_LIBRARY_PACKAGE_NAME: $e")
            throw e
        }
    }

    private fun launchKonkukLibrary() {
        try {
            val intent = Intent().apply {
                setClassName(KU_LIBRARY_PACKAGE_NAME, KU_LIBRARY_CLASS_NAME)
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, MESSAGE_APP_NOT_FOUND, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchPlayStore() {
        try {
            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(KU_LIBRARY_STORE_URI)
            }
            startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, MESSAGE_APP_NOT_FOUND, Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        fun start(activity: Activity) {
            with(activity) {
                val intent = Intent(this, LibrarySeatActivity::class.java)
                startActivity(intent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        R.anim.anim_slide_right_enter,
                        R.anim.anim_stay_exit
                    )
                } else {
                    overridePendingTransition(R.anim.anim_slide_right_enter, R.anim.anim_stay_exit)
                }
            }
        }

        private const val KU_LIBRARY_PACKAGE_NAME = "kr.ac.kku.library"
        private const val KU_LIBRARY_CLASS_NAME = "kr.ac.kku.library.MainActivity"
        private const val KU_LIBRARY_STORE_URI = "market://details?id=$KU_LIBRARY_PACKAGE_NAME"
        private const val MESSAGE_APP_NOT_FOUND = "도서관 앱을 찾을 수 없습니다."
    }
}