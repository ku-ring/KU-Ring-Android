package com.ku_stacks.ku_ring.ui.notice_webview

import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repository: NoticeRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    fun updateNoticeTobeRead(articleId: String, category: String) {
        disposable.add(
            repository.updateNoticeToBeRead(articleId, category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.e("noticeRecord update true : $articleId")
                }, {
                    Timber.e("noticeRecord update fail : $it")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}