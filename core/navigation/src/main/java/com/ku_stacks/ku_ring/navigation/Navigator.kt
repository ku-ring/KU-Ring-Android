package com.ku_stacks.ku_ring.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(SplashKey)

    fun navigate(key: NavKey) {
        backStack.add(key)
    }

    fun goBack(): Boolean {
        if (backStack.size <= 1) return false
        return backStack.removeLastOrNull() != null
    }

    fun replaceAll(key: NavKey) {
        backStack.clear()
        backStack.add(key)
    }
}
