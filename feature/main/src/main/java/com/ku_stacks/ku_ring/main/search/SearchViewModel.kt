package com.ku_stacks.ku_ring.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.search.compose.SearchState
import com.ku_stacks.ku_ring.main.search.compose.SearchTabInfo
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.search.repository.SearchHistoryRepository
import com.ku_stacks.ku_ring.staff.repository.StaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val staffRepository: StaffRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val _noticeSearchResult = MutableStateFlow<List<Notice>>(listOf())
    val noticeSearchResult: StateFlow<List<Notice>> = _noticeSearchResult.asStateFlow()

    private val _staffSearchResult = MutableStateFlow<List<Staff>>(listOf())
    val staffSearchResult: StateFlow<List<Staff>> = _staffSearchResult.asStateFlow()

    private val _searchHistories = MutableStateFlow<List<String>>(listOf())
    val searchHistories: StateFlow<List<String>> = _searchHistories.asStateFlow()

    init {
        viewModelScope.launch {
            searchHistoryRepository.getAllSearchHistory()
                .collectLatest { list ->
                    _searchHistories.update {
                        list
                    }
                }
        }
    }

    fun onSearch(searchState: SearchState) {
        if (searchState.query.isBlank()) {
            return
        }

        viewModelScope.launch {
            updateKeywordHistory(searchState.query)

            searchState.isLoading = true
            when (searchState.tab) {
                SearchTabInfo.Notice -> {
                    searchNotice(searchState.query)
                }
                SearchTabInfo.Staff -> {
                    searchProfessor(searchState.query)
                }
            }
            searchState.isLoading = false
        }
    }

    private suspend fun updateKeywordHistory(keyword: String) {
        searchHistoryRepository.addSearchHistory(keyword)
    }

    private suspend fun searchNotice(query: String) {
        val notices = noticeRepository.getNoticeSearchResult(query)
        _noticeSearchResult.update { notices }
    }

    private suspend fun searchProfessor(query: String) {
        val professors = staffRepository.searchStaff(query)
        _staffSearchResult.update { professors }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryRepository.clearSearchHistories()
        }
    }
}
