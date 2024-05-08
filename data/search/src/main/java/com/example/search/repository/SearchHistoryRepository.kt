package com.example.search.repository

interface SearchHistoryRepository {

    suspend fun addSearchHistory(keyword: String)
}
