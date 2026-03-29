package com.ku_stacks.ku_ring.edit_departments

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.edit_departments.compose.EditDepartmentsScreen
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.EditDepartmentsKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

class EditDepartmentsEntryBuilder : EntryBuilderProvider {
    override fun EntryProviderScope<NavKey>.provide() {
        entry<EditDepartmentsKey> {
            val activity = LocalActivity.current
            EditDepartmentsScreen(
                onClose = { activity?.finish() },
                modifier = Modifier
                    .fillMaxSize()
                    .background(KuringTheme.colors.background),
            )
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object EditDepartmentsEntryBuilderModule {
    @Provides
    @IntoSet
    fun provideEditDepartmentsEntryBuilder(): EntryBuilderProvider = EditDepartmentsEntryBuilder()
}
