package com.ku_stacks.ku_ring.space.repository

import com.ku_stacks.ku_ring.remote.space.KuringSpaceClient
import com.ku_stacks.ku_ring.util.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class KuringSpaceRepositoryImpl @Inject constructor(
    private val kuringSpaceClient: KuringSpaceClient,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : KuringSpaceRepository {
    override suspend fun getMinimumAppVersion(): String {
        return withContext(ioDispatcher) {
            kuringSpaceClient.getMinimumAppVersion()
        }
    }
}