package com.ku_stacks.ku_ring.space.repository

import com.ku_stacks.ku_ring.remote.space.KuringSpaceClient
import javax.inject.Inject

class KuringSpaceRepositoryImpl @Inject constructor(
    private val kuringSpaceClient: KuringSpaceClient,
) : KuringSpaceRepository {
    override suspend fun getMinimumAppVersion(): String {
        return kuringSpaceClient.getMinimumAppVersion()
    }
}