package com.ku_stacks.ku_ring.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.search.compose.SearchState
import com.ku_stacks.ku_ring.main.search.compose.SearchTabInfo
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.staff.repository.StaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val staffRepository: StaffRepository,
) : ViewModel() {

    private val _staffList = MutableLiveData<List<Staff>>()
    val staffList: LiveData<List<Staff>>
        get() = _staffList

    private val _noticeSearchResult = MutableStateFlow<List<Notice>>(listOf())
    val noticeSearchResult: StateFlow<List<Notice>> = _noticeSearchResult.asStateFlow()

    private val _staffSearchResult = MutableStateFlow<List<Staff>>(listOf())
    val staffSearchResult: StateFlow<List<Staff>> = _staffSearchResult.asStateFlow()

    init {
        Timber.e("SearchViewModel init")
    }

    fun onClickSearch(searchState: SearchState) {
        when (searchState.tab) {
            SearchTabInfo.Notice -> {
                searchNotice(searchState.query)
            }
            SearchTabInfo.Staff -> {
                searchProfessor(searchState.query)
            }
        }
    }

    private fun searchNotice(query: String) {
        viewModelScope.launch {
            val notices = noticeRepository.getNoticeSearchResult(query)
            _noticeSearchResult.update { notices }
        }
    }

    private fun searchProfessor(query: String) {
        viewModelScope.launch {
            val professors = staffRepository.searchStaff(query)
            _staffSearchResult.update { professors }
        }
    }
}
