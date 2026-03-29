package com.ku_stacks.ku_ring.library

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.library.compose.LibrarySeatScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.LibrarySeatKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import timber.log.Timber

class LibrarySeatEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<LibrarySeatKey> {
            val context = LocalContext.current
            LibrarySeatScreen(
                onNavigateBack = { (context as? Activity)?.finish() },
                onLaunchLibraryIntent = {
                    try {
                        context.packageManager.getPackageInfo(
                            KU_LIBRARY_PACKAGE_NAME,
                            PackageManager.MATCH_UNINSTALLED_PACKAGES
                        )
                        try {
                            val intent = Intent().apply {
                                setClassName(KU_LIBRARY_PACKAGE_NAME, KU_LIBRARY_CLASS_NAME)
                            }
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, MESSAGE_APP_NOT_FOUND, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        Timber.e("PackageManager could not find $KU_LIBRARY_PACKAGE_NAME: $e")
                        try {
                            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(KU_LIBRARY_STORE_URI)
                            }
                            context.startActivity(playStoreIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, MESSAGE_APP_NOT_FOUND, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    private companion object {
        const val KU_LIBRARY_PACKAGE_NAME = "kr.ac.kku.library"
        const val KU_LIBRARY_CLASS_NAME = "kr.ac.kku.library.MainActivity"
        const val KU_LIBRARY_STORE_URI = "market://details?id=$KU_LIBRARY_PACKAGE_NAME"
        const val MESSAGE_APP_NOT_FOUND = "도서관 앱을 찾을 수 없습니다."
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LibrarySeatEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideLibrarySeatEntryBuilder(): EntryBuilderProvider = LibrarySeatEntryBuilder()
}
