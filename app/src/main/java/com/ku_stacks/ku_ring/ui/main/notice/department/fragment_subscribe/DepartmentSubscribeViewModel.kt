package com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentSubscribeViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : ViewModel() {

    val searchKeyword = MutableStateFlow<String>("")

    private val _departmentDataStateFlow = MutableStateFlow<DepartmentStateData>(DepartmentStateData.Loading)
    val departmentStateFlow = _departmentDataStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            departmentRepository.insertAllDepartmentsFromRemote()
        }

        searchKeyword.onEach { loadFilteredDepartment(it) }
            .launchIn(viewModelScope)
    }


    private fun loadFilteredDepartment(keyword: String) {
        viewModelScope.launch {
            val result = departmentRepository.getDepartmentsByKoreanName(keyword)
            _departmentDataStateFlow.value = DepartmentStateData.Success(result)
        }
    }
}
