package com.ku_stacks.ku_ring.main.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.reactive.asFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryNoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private var _noticesFlow: MutableStateFlow<Flow<PagingData<Notice>>?> = MutableStateFlow(null)
    val noticesFlow: StateFlow<Flow<PagingData<Notice>>?>
        get() = _noticesFlow

    fun getNotices(shortCategory: String) {
        Timber.d("set short category as $shortCategory, viewModel hashCode: ${hashCode()}")
        _noticesFlow.value = noticeRepository.getNotices(shortCategory, viewModelScope).asFlow()
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
            Timber.e("compositeDisposable disposed")
        }
    }

}