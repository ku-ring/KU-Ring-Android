package com.ku_stacks.ku_ring.main.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentNoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val departmentRepository: DepartmentRepository,
) : ViewModel() {

    private val _subscribedDepartments = MutableStateFlow(emptyList<Department>())
    val subscribedDepartments: StateFlow<List<Department>>
        get() = _subscribedDepartments

    private val isInitialLoading = MutableStateFlow(true)

    val departmentNoticeScreenState: StateFlow<DepartmentNoticeScreenState> = combine(
        isInitialLoading,
        subscribedDepartments,
    ) { isInitialLoading, subscribedDepartments ->
        when {
            isInitialLoading -> DepartmentNoticeScreenState.InitialLoading
            subscribedDepartments.isEmpty() -> DepartmentNoticeScreenState.DepartmentsEmpty
            else -> DepartmentNoticeScreenState.DepartmentsNotEmpty
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DepartmentNoticeScreenState.InitialLoading
    )

    val currentDepartmentNotice: StateFlow<Flow<PagingData<Notice>>?> =
        subscribedDepartments.map { departments ->
            val selectedDepartment = departments.firstOrNull { it.isSelected }
            if (selectedDepartment == null) {
                null
            } else {
                noticeRepository.getDepartmentNotices(selectedDepartment.shortName)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        collectSubscribedDepartments()
    }

    private fun collectSubscribedDepartments() {
        viewModelScope.launch {
            departmentRepository.getSubscribedDepartmentsAsFlow().collectLatest { departments ->
                _subscribedDepartments.value = departments
                isInitialLoading.value = false
            }
        }
    }

    fun selectDepartment(department: Department) {
        viewModelScope.launch {
            departmentRepository.clearMainDepartments()
            departmentRepository.updateMainDepartmentStatus(department.name, true)
        }
    }
}

sealed class DepartmentNoticeScreenState {
    object InitialLoading : DepartmentNoticeScreenState()
    object DepartmentsEmpty : DepartmentNoticeScreenState()
    object DepartmentsNotEmpty : DepartmentNoticeScreenState()
}