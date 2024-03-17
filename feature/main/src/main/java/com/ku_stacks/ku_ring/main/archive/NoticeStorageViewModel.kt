package com.ku_stacks.ku_ring.main.archive

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeStorageViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {

    private val _savedNotices = MutableStateFlow<List<Notice>>(emptyList())
    val savedNotices: StateFlow<List<Notice>>
        get() = _savedNotices

    var isSelectedModeEnabled by mutableStateOf(false)
        private set

    /**
     * key: [Notice.articleId], value: [Notice.category]
     */
    private val _selectedNotices = MutableStateFlow<Map<String, String>>(emptyMap())
    private val selectedNotices: Map<String, String>
        get() = _selectedNotices.value

    val selectedNoticeIds: StateFlow<Set<String>> = _selectedNotices.map {
        it.keys
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    val isAllNoticesSelected = _selectedNotices.map {
        it.size == savedNotices.value.size
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        viewModelScope.launch {
            noticeRepository.getSavedNotices().collect { savedNotices ->
                _savedNotices.value = savedNotices
            }
        }
    }

    fun setSelectedMode(value: Boolean) {
        isSelectedModeEnabled = value
        if (!value) {
            updateSelectedNotices {
                emptyMap()
            }
        }
    }

    fun selectAllNotices() {
        val allNoticesPair = savedNotices.value.map { notice ->
            Pair(notice.articleId, notice.category)
        }
        updateSelectedNotices {
            selectedNotices.plus(allNoticesPair)
        }
    }

    fun updateNoticeAsReadOnStorage(notice: Notice) {
        viewModelScope.launch {
            noticeRepository.updateNoticeToBeReadOnStorage(notice.articleId, notice.category)
        }
    }

    fun toggleNoticeSelection(notice: Notice) {
        val articleId = notice.articleId
        updateSelectedNotices {
            if (articleId in selectedNotices) {
                selectedNotices.minus(articleId)
            } else {
                selectedNotices.plus(articleId to notice.category)
            }
        }
    }

    fun deleteNotices() {
        isSelectedModeEnabled = false
        viewModelScope.launch {
            var deletedNotices = 0
            selectedNotices.forEach { (articleId, category) ->
                noticeRepository.updateSavedStatus(articleId, category, false)
                deletedNotices++
            }
            Timber.d("$deletedNotices notice(s) were deleted.")
            updateSelectedNotices {
                emptyMap()
            }
        }
    }

    private fun updateSelectedNotices(block: Map<String, String>.() -> Map<String, String>) {
        _selectedNotices.value = block(selectedNotices)
    }

    fun clearNotices() {
        viewModelScope.launch {
            noticeRepository.clearSavedNotices()
        }
    }
}