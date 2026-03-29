package com.ku_stacks.ku_ring

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.ku_stacks.ku_ring.navigation.EntryBuilderProvider
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HostActivity : ComponentActivity() {

    @Inject
    lateinit var entryBuilders: @JvmSuppressWildcards Set<EntryBuilderProvider>

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(SplashKey)
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entryBuilders.forEach { builder -> with(builder) { provide() } }
                }
            )
        }
    }
}
