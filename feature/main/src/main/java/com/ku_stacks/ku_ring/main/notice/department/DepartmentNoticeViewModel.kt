package com.ku_stacks.ku_ring.main.notice.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.util.modifyList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        Timber.d("department screen state: $isInitialLoading, ${subscribedDepartments.size}")
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
                val beforeSelected = subscribedDepartments.value.firstOrNull { it.isSelected }

                val markedDepartment =
                    if (beforeSelected != null && departments.containsSelected(beforeSelected)) {
                        departments.markDepartment(beforeSelected)
                    } else {
                        departments.selectFirstDepartment()
                    }

                _subscribedDepartments.value = markedDepartment
                isInitialLoading.value = false
            }
        }
    }

    private fun List<Department>.containsSelected(selected: Department) =
        this.any { it.koreanName == selected.koreanName }

    private fun List<Department>.selectFirstDepartment(): List<Department> {
        return if (this.isEmpty()) {
            this
        } else {
            this.toMutableList().apply {
                this[0] = this[0].copy(isSelected = true)
            }
        }
    }

    private fun List<Department>.markDepartment(department: Department): List<Department> {
        return this.map {
            if (it.koreanName == department.koreanName) {
                it.copy(isSelected = true)
            } else {
                it
            }
        }
    }

    fun selectDepartment(department: Department) {
        val currentSelectedIndex = subscribedDepartments.value.indexOfFirst { it.isSelected }
        val index =
            subscribedDepartments.value.indexOfFirst { it.koreanName == department.koreanName }
        if (currentSelectedIndex == index) return

        _subscribedDepartments.modifyList {
            if (index != -1) {
                this[index] = this[index].copy(isSelected = true)
            }
            if (currentSelectedIndex != -1) {
                this[currentSelectedIndex] = this[currentSelectedIndex].copy(isSelected = false)
            }
        }
    }
}

sealed class DepartmentNoticeScreenState {
    object InitialLoading : DepartmentNoticeScreenState()
    object DepartmentsEmpty : DepartmentNoticeScreenState()
    object DepartmentsNotEmpty : DepartmentNoticeScreenState()
}