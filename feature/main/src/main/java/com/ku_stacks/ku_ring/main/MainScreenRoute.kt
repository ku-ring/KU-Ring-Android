package com.ku_stacks.ku_ring.main

import kotlinx.serialization.Serializable

@Serializable
sealed interface MainScreenRoute {
    val route: String

    @Serializable
    data object Notice : MainScreenRoute {
        override val route: String
            get() =
                this::class.java.canonicalName
                    ?.toString()
                    .toString()
    }

    @Serializable
    data object Archive : MainScreenRoute {
        override val route: String
            get() =
                this::class.java.canonicalName
                    ?.toString()
                    .toString()
    }

    @Serializable
    data object CampusMap : MainScreenRoute {
        override val route: String
            get() =
                this::class.java.canonicalName
                    ?.toString()
                    .toString()
    }

    @Serializable
    data object Settings : MainScreenRoute {
        override val route: String
            get() =
                this::class.java.canonicalName
                    ?.toString()
                    .toString()
    }

    companion object {
        val entries = listOf(Notice, Archive, CampusMap, Settings)

        fun of(route: String): MainScreenRoute =
            entries.firstOrNull { it.route == route }
                ?: throw IllegalArgumentException("Unknown route: $route")
    }
}
