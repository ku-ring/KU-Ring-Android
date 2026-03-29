package com.ku_stacks.ku_ring.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

interface EntryBuilderProvider {
    fun EntryProviderScope<NavKey>.provide()
}
