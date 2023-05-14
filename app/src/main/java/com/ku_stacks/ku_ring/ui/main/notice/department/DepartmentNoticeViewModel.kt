package com.ku_stacks.ku_ring.ui.main.notice.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.model.Department
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.repository.DepartmentNoticeRepository
import com.ku_stacks.ku_ring.repository.DepartmentRepository
import com.ku_stacks.ku_ring.util.modifyList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentNoticeViewModel @Inject constructor(
    private val departmentNoticeRepository: DepartmentNoticeRepository,
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

    private var _currentDepartmentNotice: MutableStateFlow<Flow<PagingData<Notice>>> =
        MutableStateFlow(departmentNoticeRepository.getDepartmentNotices("empty dept"))
    val currentDepartmentNotice: StateFlow<Flow<PagingData<Notice>>>
        get() = _currentDepartmentNotice


    init {
        collectSubscribedDepartments()
    }

    private fun collectSubscribedDepartments() {
        viewModelScope.launch {
            departmentRepository.getSubscribedDepartmentsAsFlow().collectLatest { departments ->
                val sortedDepartments = departments.sortedBy { it.koreanName }
                _subscribedDepartments.value = sortedDepartments
                selectFirstDepartmentIfInitialLoad()

                isInitialLoading.value = false
            }
        }
    }

    private fun selectFirstDepartmentIfInitialLoad() {
        if (isInitialLoading.value && _subscribedDepartments.value.isNotEmpty()) {
            selectDepartment(_subscribedDepartments.value.first())
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
        _currentDepartmentNotice.value =
            departmentNoticeRepository.getDepartmentNotices(department.shortName)
    }
}

sealed class DepartmentNoticeScreenState {
    object InitialLoading : DepartmentNoticeScreenState()
    object DepartmentsEmpty : DepartmentNoticeScreenState()
    object DepartmentsNotEmpty : DepartmentNoticeScreenState()
}