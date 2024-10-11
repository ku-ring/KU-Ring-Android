package com.ku_stacks.ku_ring.main.notice

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryNoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {
    private var _noticesFlow: MutableStateFlow<Flow<PagingData<Notice>>?> = MutableStateFlow(null)
    val noticesFlow: StateFlow<Flow<PagingData<Notice>>?>
        get() = _noticesFlow

    fun getNotices(shortCategory: String) {
        _noticesFlow.value = noticeRepository.getNotices(shortCategory)
    }
}
