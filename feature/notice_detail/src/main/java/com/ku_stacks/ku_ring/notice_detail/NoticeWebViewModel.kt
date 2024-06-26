package com.ku_stacks.ku_ring.notice_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeWebViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val webViewNotice =
        savedStateHandle.get(WebViewNotice.EXTRA_KEY) as? WebViewNotice

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
        disposable.add(
            noticeRepository.updateNoticeToBeRead(webViewNotice.articleId, webViewNotice.category)
                .subscribeOn(Schedulers.io())
                .subscribe({ }, { })
        )
    }

    fun onSaveButtonClick() {
        if (webViewNotice == null) return
        viewModelScope.launch {
            noticeRepository.updateSavedStatus(
                webViewNotice.articleId,
                webViewNotice.category,
                !isSaved.value
            )
        }
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}