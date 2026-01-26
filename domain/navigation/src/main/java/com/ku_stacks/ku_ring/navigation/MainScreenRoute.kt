package com.ku_stacks.ku_ring.navigation

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainScreenRoute : KuringRoute {

    @Serializable
    data object Notice : MainScreenRoute

    @Serializable
    data object Calendar : MainScreenRoute

    @Serializable
    data object CampusMap : MainScreenRoute

    @Serializable
    data object Club : MainScreenRoute

    @Serializable
    data object Settings : MainScreenRoute

    companion object {
        val entries = listOf(Notice, Calendar, CampusMap, Club, Settings)

        fun of(entry: NavBackStackEntry): MainScreenRoute = of(entry.destination.route.orEmpty())

        fun of(route: String): MainScreenRoute = entries.firstOrNull { it.route == route }
            ?: throw IllegalArgumentException("Unknown route: $route")
    }
}