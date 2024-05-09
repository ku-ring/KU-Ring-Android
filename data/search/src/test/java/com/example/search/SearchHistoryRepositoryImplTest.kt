package com.example.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.search.repository.SearchHistoryRepositoryImpl
import com.ku_stacks.ku_ring.local.entity.SearchHistoryEntity
import com.ku_stacks.ku_ring.local.room.SearchHistoryDao
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


class SearchHistoryRepositoryImplTest {

    private lateinit var repository: SearchHistoryRepositoryImpl

    private val dao: SearchHistoryDao = Mockito.mock(SearchHistoryDao::class.java)

    private val sampleKeyword = "ABC"

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = SearchHistoryRepositoryImpl(dao)
    }

    @Test
    fun `최초 addSearchHistory 호출 시 데이터를 insert 한다`() = runTest {
        // Given
        Mockito.`when`(dao.getEntityOrNull(any())).thenReturn(null)

        // When
        repository.addSearchHistory(sampleKeyword)

        // Then
        verify(dao, times(1)).getEntityOrNull(any())
        verify(dao, times(0)).delete(any())
        verify(dao, times(1)).insert(any())
    }

    @Test
    fun `키워드가 이미 있을때 addSearchHistory 호출 시 기존 데이터를 삭제후 insert 한다`() = runTest {
        // Given
        Mockito.`when`(dao.getEntityOrNull(any())).thenReturn(SearchHistoryEntity(123, "ABC"))

        repository.addSearchHistory(sampleKeyword)
        clearInvocations(dao)

        // When
        repository.addSearchHistory(sampleKeyword)

        // Then
        verify(dao, times(1)).getEntityOrNull(any())
        verify(dao, times(1)).delete(any())
        verify(dao, times(1)).insert(any())
    }
}
