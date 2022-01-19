package com.ku_stacks.ku_ring.ui.home.category._3_nation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NationViewModel @Inject constructor(
    private val repository: NoticeRepository
): ViewModel() {

    private var currentNoticeResult: Flowable<PagingData<Notice>>? = null

    init {
        Timber.e("NationViewModel injected")
    }

    fun getNotices(): Flowable<PagingData<Notice>> {
        val lastResult = currentNoticeResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult = repository.getNotices("nat", viewModelScope)
        currentNoticeResult = newResult
        return newResult
    }
}