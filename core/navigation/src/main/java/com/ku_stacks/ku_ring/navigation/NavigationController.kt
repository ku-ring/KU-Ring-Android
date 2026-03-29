package com.ku_stacks.ku_ring.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Abstraction for controlling the navigation back stack.
 * Injected into KuringNavigatorImpl to decouple navigation from Activity lifecycle.
 */
interface NavigationController {
    fun navigate(key: NavKey)
    fun popBackStack()
}
