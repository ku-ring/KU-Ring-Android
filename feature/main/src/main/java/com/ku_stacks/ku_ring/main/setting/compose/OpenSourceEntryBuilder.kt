package com.ku_stacks.ku_ring.main.setting.compose

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.inner_screen.OpenSourceScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.OpenSourceKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class OpenSourceEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<OpenSourceKey> {
            val navigator = LocalNavigator.current
            val activity = LocalActivity.current
            val context = LocalContext.current
            OpenSourceScreen(
                closeOpenSourceScreen = { activity?.finish() },
                navigateToOssLicenses = {
                    activity?.let { navigator.navigateToOssLicensesMenu(it) }
                },
                navigateToLottieFiles = {
                    val url = context.getString(R.string.lottie_files)
                    activity?.let { navigator.navigateToNotionView(it, url) }
                },
                navigateToTossFaceCopyright = {
                    val url = context.getString(R.string.toss_face_copyright)
                    activity?.let { navigator.navigateToNotionView(it, url) }
                },
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object OpenSourceEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideOpenSourceEntryBuilder(): EntryBuilderProvider = OpenSourceEntryBuilder()
}
