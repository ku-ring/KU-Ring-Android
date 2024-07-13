package com.ku_stacks.ku_ring.ui_util

interface KuringRoute {
    val route: String
        get() = this::class.java.canonicalName?.toString().toString()
}