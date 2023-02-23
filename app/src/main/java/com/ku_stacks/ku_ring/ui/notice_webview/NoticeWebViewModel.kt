package com.ku_stacks.ku_ring.ui.notice_webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
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

    private val disposable = CompositeDisposable()

    private val articleId = savedStateHandle.getString(NoticeWebActivity.NOTICE_ARTICLE_ID)
    private val category = savedStateHandle.getString(NoticeWebActivity.NOTICE_CATEGORY)
    private val url = savedStateHandle.getString(NoticeWebActivity.NOTICE_URL)
    private val postedDate = savedStateHandle.getString(NoticeWebActivity.NOTICE_POSTED_DATE)
    private val subject = savedStateHandle.getString(NoticeWebActivity.NOTICE_SUBJECT)

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean>
        get() = _isSaved

    init {
        viewModelScope.launch {
            noticeRepository.getSavedNotices().collect { savedNotices ->
                _isSaved.value = savedNotices.any { it.articleId == articleId }
            }
        }
    }

    fun updateNoticeTobeRead(articleId: String, category: String) {
        disposable.add(
            noticeRepository.updateNoticeToBeRead(articleId, category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.e("noticeRecord update true : $articleId")
                }, {
                    Timber.e("noticeRecord update fail : $it")
                })
        )
    }

    fun onSaveButtonClick() {
        if (articleId == null || category == null || url == null || postedDate == null || subject == null) return
        viewModelScope.launch {
            noticeRepository.updateSavedStatus(articleId, category, !isSaved.value)
        }
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun SavedStateHandle.getString(key: String) = get<String>(key)
}