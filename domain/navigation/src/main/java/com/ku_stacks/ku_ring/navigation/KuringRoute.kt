package com.ku_stacks.ku_ring.navigation

interface KuringRoute {
    val route: String
        get() = this::class.java.canonicalName.toString()
}