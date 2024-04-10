package com.ku_stacks.ku_ring.space.repository

interface KuringSpaceRepository {
    suspend fun getMinimumAppVersion(): String
}