package com.ku_stacks.ku_ring.main

import kotlinx.serialization.Serializable

sealed interface MainScreenRoute {
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
    }
}