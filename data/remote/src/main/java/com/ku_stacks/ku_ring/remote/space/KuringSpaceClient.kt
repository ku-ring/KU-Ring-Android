package com.ku_stacks.ku_ring.remote.space

import javax.inject.Inject

class KuringSpaceClient @Inject constructor(private val kuringSpaceService: KuringSpaceService) {
    suspend fun getMinimumAppVersion(): String =
        kuringSpaceService.getMinimumAppVersion().minimumAppVersion
}