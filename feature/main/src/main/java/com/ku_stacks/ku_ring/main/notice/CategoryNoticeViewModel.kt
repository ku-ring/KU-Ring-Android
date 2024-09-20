package com.ku_stacks.ku_ring.main.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryNoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {
    private var _noticesFlow: MutableStateFlow<Flow<PagingData<Notice>>?> = MutableStateFlow(null)
    val noticesFlow: StateFlow<Flow<PagingData<Notice>>?>
        get() = _noticesFlow

    private val articleIds = mutableSetOf<String>()

    fun getNotices(shortCategory: String) {
        _noticesFlow.value = noticeRepository.getNotices(shortCategory, viewModelScope)
    }

    fun insertNoticeToLocal(notice: Notice) {
        // 2.1 TODO: 학과별 공지처럼 Mediator 사용하도록 수정하고, NoticeRepository.getNotices() 등 레거시 제거
        if (notice.articleId !in articleIds) {
            articleIds.add(notice.articleId)
            viewModelScope.launch {
                noticeRepository.insertNotice(notice)
            }
        }
    }
}
