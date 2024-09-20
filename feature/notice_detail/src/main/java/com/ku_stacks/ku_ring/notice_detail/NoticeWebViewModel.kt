package com.ku_stacks.ku_ring.notice_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeWebViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val webViewNotice: WebViewNotice? by lazy { savedStateHandle[WebViewNotice.EXTRA_KEY] }

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean>
        get() = _isSaved

    init {
        viewModelScope.launch {
            noticeRepository.getSavedNotices().collect { savedNotices ->
                _isSaved.value = savedNotices.any { it.articleId == webViewNotice?.articleId }
            }
        }
    }

    fun updateNoticeTobeRead(webViewNotice: WebViewNotice) {
        viewModelScope.launch {
            runCatching {
                noticeRepository.updateNoticeToBeReadOnStorage(webViewNotice.articleId, webViewNotice.category)
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    fun onSaveButtonClick() {
        if (webViewNotice == null) return
        viewModelScope.launch {
            noticeRepository.updateSavedStatus(
                webViewNotice?.articleId.orEmpty(),
                webViewNotice?.category.orEmpty(),
                !isSaved.value
            )
        }
    }
}