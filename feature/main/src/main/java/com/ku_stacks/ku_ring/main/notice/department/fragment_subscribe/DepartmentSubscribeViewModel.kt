package com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentSubscribeViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : ViewModel() {

    val searchKeyword = MutableStateFlow("")

    private val _departmentDataStateFlow = MutableStateFlow<DepartmentStateData>(DepartmentStateData.Loading)
    val departmentStateFlow = _departmentDataStateFlow.asStateFlow()

    val emptyResultVisible = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            departmentRepository.updateDepartmentsFromRemote()
        }

        searchKeyword.onEach { loadFilteredDepartment(it) }
            .launchIn(viewModelScope)

        _departmentDataStateFlow.filterIsInstance<DepartmentStateData.Success>()
            .onEach { emptyResultVisible.value = it.list.isEmpty() }
            .launchIn(viewModelScope)
    }

    private fun loadFilteredDepartment(keyword: String) {
        viewModelScope.launch {
            val result = departmentRepository.getDepartmentsByKoreanName(keyword)
            _departmentDataStateFlow.value = DepartmentStateData.Success(result)
        }
    }

    fun onSubscribeClick(department: Department) {
        viewModelScope.launch {
            val newStats = !department.isSubscribed
            departmentRepository.updateSubscribeStatus(department.name, newStats)

            loadFilteredDepartment(searchKeyword.value)
        }
    }
}
