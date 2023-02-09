package com.ku_stacks.ku_ring.ui.notice_webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.data.model.SavedNotice
import com.ku_stacks.ku_ring.di.IODispatcher
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.SavedNoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeWebViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val savedNoticeRepository: SavedNoticeRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
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
            savedNoticeRepository.getSavedNotices().collectLatest { savedNotices ->
                _isSaved.value = savedNotices.any { it.articleId == articleId }
            }
        }
    }

    fun updateNoticeTobeRead(articleId: String) {
        disposable.add(
            noticeRepository.updateNoticeToBeRead(articleId)
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
            if (isSaved.value) {
                savedNoticeRepository.deleteNotice(articleId)
            } else {
                val baseUrl = url.substringBefore('?')
                savedNoticeRepository.saveNotice(
                    SavedNotice(articleId, category, baseUrl, postedDate, subject)
                )
            }
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