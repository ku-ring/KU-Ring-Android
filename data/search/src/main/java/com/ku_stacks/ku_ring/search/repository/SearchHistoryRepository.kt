package com.ku_stacks.ku_ring.search.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun addSearchHistory(keyword: String)

    suspend fun getAllSearchHistory(): Flow<List<String>>

    suspend fun clearSearchHistories()
}
