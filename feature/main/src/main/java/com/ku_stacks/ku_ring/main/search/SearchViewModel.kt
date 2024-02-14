package com.ku_stacks.ku_ring.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.staff.repository.StaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val staffRepository: StaffRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _staffList = MutableLiveData<List<Staff>>()
    val staffList: LiveData<List<Staff>>
        get() = _staffList

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeList: LiveData<List<Notice>>
        get() = _noticeList

    init {
        Timber.e("SearchViewModel init")
    }

    fun searchStaff(keyword: String) {
        Timber.e("search staff $keyword")
        disposable.add(
            staffRepository.searchStaff(keyword)
                .subscribe(
                    { _staffList.postValue(it) },
                    { Timber.e("search staff error: $it") }
                )
        )
    }

    fun searchNotice(keyword: String) {
        Timber.e("search notice $keyword")
        disposable.add(
            noticeRepository.searchNotice(keyword)
                .map { noticeList -> markSavedNotices(noticeList) }
                .subscribe(
                    { _noticeList.postValue(it) },
                    { Timber.e("search notice error: $it") }
                )
        )
    }

    fun clearNoticeList() {
        _noticeList.postValue(emptyList())
    }

    fun clearStaffList() {
        _staffList.postValue(emptyList())
    }

    private fun markSavedNotices(notices: List<Notice>): List<Notice> {
        val savedNotice2 = noticeRepository.getSavedNoticeList()
            .map { it.articleId }

        return notices.map {
            it.copy(isSaved = savedNotice2.contains(it.articleId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}
