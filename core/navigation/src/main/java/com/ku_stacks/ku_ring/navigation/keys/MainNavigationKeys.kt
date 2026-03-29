package com.ku_stacks.ku_ring.navigation.keys

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data class MainHubKey(val startTab: String = "Notice") : NavKey
@Serializable data object ArchiveKey : NavKey
@Serializable data object SearchKey : NavKey
@Serializable data object OpenSourceKey : NavKey
