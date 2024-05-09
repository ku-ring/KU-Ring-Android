package com.example.search.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun addSearchHistory(keyword: String)

    suspend fun getAllSearchHistory(): Flow<List<String>>

    suspend fun clearSearchHistories()
}
