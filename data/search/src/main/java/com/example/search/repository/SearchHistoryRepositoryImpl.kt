package com.example.search.repository

import com.ku_stacks.ku_ring.local.entity.SearchHistoryEntity
import com.ku_stacks.ku_ring.local.room.SearchHistoryDao
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
) : SearchHistoryRepository {
    override suspend fun insert(keyword: String) {
        searchHistoryDao.insert(
            SearchHistoryEntity(keyword = keyword)
        )
    }
}
