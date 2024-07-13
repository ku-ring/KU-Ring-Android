package com.ku_stacks.ku_ring.main

import com.ku_stacks.ku_ring.ui_util.KuringRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainScreenRoute : KuringRoute {

    @Serializable
    data object Notice : MainScreenRoute

    @Serializable
    data object Archive : MainScreenRoute

    @Serializable
    data object CampusMap : MainScreenRoute

    @Serializable
    data object Settings : MainScreenRoute

    companion object {
        val entries = listOf(Notice, Archive, CampusMap, Settings)

        fun of(route: String): MainScreenRoute =
            entries.firstOrNull { it.route == route }
                ?: throw IllegalArgumentException("Unknown route: $route")
    }
}
