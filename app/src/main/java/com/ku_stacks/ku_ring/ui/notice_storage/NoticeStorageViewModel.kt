package com.ku_stacks.ku_ring.ui.notice_storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
        viewModelScope.launch {
            noticeRepository.getSavedNotices().collect { savedNotices ->
                _savedNotices.value = savedNotices
            }
        }
    }

    fun updateNoticeAsReadOnStorage(articleId: String, category: String) {
        viewModelScope.launch {
            noticeRepository.updateNoticeToBeReadOnStorage(articleId, category)
        }
    }

    fun deleteNotice(articleId: String, category: String) {
        viewModelScope.launch {
            noticeRepository.updateSavedStatus(articleId, category, false)
            Timber.d("Notice $articleId deleted.")
        }
    }

    fun clearNotices() {
        viewModelScope.launch {
            noticeRepository.clearSavedNotices()
        }
    }
}