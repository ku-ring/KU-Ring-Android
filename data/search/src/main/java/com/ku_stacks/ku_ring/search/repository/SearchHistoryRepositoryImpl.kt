package com.ku_stacks.ku_ring.search.repository

import com.ku_stacks.ku_ring.local.entity.SearchHistoryEntity
import com.ku_stacks.ku_ring.local.room.SearchHistoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
) : SearchHistoryRepository {
    override suspend fun addSearchHistory(keyword: String) {
        val previousEntity = searchHistoryDao.getEntityOrNull(keyword)

        previousEntity?.let {
            searchHistoryDao.delete(it)
        }

        searchHistoryDao.insert(
            SearchHistoryEntity(keyword = keyword)
        )
    }

    override suspend fun getAllSearchHistory(): Flow<List<String>> {
        return searchHistoryDao.getAllSearchHistory()
    }

    override suspend fun clearSearchHistories() {
        searchHistoryDao.deleteAll()
    }
}
