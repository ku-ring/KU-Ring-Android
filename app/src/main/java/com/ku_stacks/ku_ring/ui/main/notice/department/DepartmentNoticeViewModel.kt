package com.ku_stacks.ku_ring.ui.main.notice.department

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DepartmentNoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    private val currentNotices: HashMap<String, Flow<PagingData<Notice>>> = hashMapOf()

    fun getDepartmentNotices(shortName: String): Flow<PagingData<Notice>> {
        if (shortName !in currentNotices) {
            currentNotices[shortName] = noticeRepository.getDepartmentNotices(shortName)
        }
        return currentNotices[shortName]!!
    }
}