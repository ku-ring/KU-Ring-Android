package com.ku_stacks.ku_ring.ui.main.notice.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticesChildViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private var currentNotices: Flowable<PagingData<Notice>>? = null

    fun insertNoticeAsOld(notice: Notice) {
        disposable.add(
            noticeRepository.insertNoticeAsOld(notice)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("noticeRecord Insert true : $articleId")
                }, { Timber.e("noticeRecord Insert fail $it") })
        )
    }

    fun getNotices(shortCategory: String): Flowable<PagingData<Notice>> {
        if (currentNotices == null) {
            currentNotices = noticeRepository.getNotices(shortCategory, viewModelScope)
        }
        return currentNotices!!
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
            Timber.e("compositeDisposable disposed")
        }
    }

}