package com.ku_stacks.ku_ring.feedback.feedback

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.compose.locals.LocalNavigator
import com.ku_stacks.ku_ring.feedback.feedback.compose.FeedbackScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.FeedbackKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class FeedbackEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<FeedbackKey> {
            val viewModel = hiltViewModel<FeedbackViewModel>()
            val navigator = LocalNavigator.current
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                viewModel.quit.collect {
                    navigator.goBack()
                }
            }
            LaunchedEffect(Unit) {
                viewModel.toast.collect { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            LaunchedEffect(Unit) {
                viewModel.toastByResource.collect { resId ->
                    Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
                }
            }

            FeedbackScreen(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FeedbackEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideFeedbackEntryBuilder(): EntryBuilderProvider = FeedbackEntryBuilder()
}
