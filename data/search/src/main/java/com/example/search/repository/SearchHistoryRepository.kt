package com.example.search.repository

interface SearchHistoryRepository {

    suspend fun insert(keyword: String)
}
