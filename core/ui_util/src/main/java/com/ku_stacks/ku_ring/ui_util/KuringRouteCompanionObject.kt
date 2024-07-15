package com.ku_stacks.ku_ring.ui_util

import androidx.navigation.NavBackStackEntry

interface KuringRouteCompanionObject<T : KuringRoute> {
    val entries: List<T>

    fun contains(entry: NavBackStackEntry): Boolean = entries.any { routeEntry ->
        routeEntry.route == entry.route
    }

    fun fromNavBackStackEntry(entry: NavBackStackEntry): T =
        entry.route.let { route ->
            entries.firstOrNull { it.route == route }
                ?: throw IllegalArgumentException("Unknown route: $route")
        }

    private val NavBackStackEntry.route: String
        get() = destination.route.orEmpty()
}