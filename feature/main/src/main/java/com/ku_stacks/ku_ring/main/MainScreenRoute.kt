package com.ku_stacks.ku_ring.main

sealed interface MainScreenRoute {
    data object Notice : MainScreenRoute
    data object Archive : MainScreenRoute
    data object CampusMap : MainScreenRoute
    data object Settings : MainScreenRoute

    companion object {
        val entries = listOf(Notice, Archive, CampusMap, Settings)
    }
}