package com.ku_stacks.ku_ring.onboarding.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
) : ViewModel() {
    private val _query = MutableStateFlow<String?>(null)
    val query: StateFlow<String?>
        get() = _query

    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    val departments: StateFlow<List<Department>>
        get() = _departments

    private val _isInitialSearch = MutableStateFlow(true)
    val isInitialSearch: StateFlow<Boolean>
        get() = _isInitialSearch

    private val _selectedDepartment = MutableStateFlow<Department?>(null)
    val selectedDepartment: StateFlow<Department?>
        get() = _selectedDepartment

    init {
        viewModelScope.launch {
            departmentRepository.updateDepartmentsFromRemote()
        }
    }

    fun onQueryUpdate(newQuery: String) {
        _query.value = newQuery
    }

    fun search() {
        _isInitialSearch.value = false
        viewModelScope.launch {
            _departments.value =
                departmentRepository.getDepartmentsByKoreanName(query.value ?: return@launch)
        }
    }

    fun selectDepartment(department: Department) {
        _selectedDepartment.value = department
    }

    fun subscribeSelectedDepartment() {
        val selectedDepartment = selectedDepartment.value ?: return

        viewModelScope.launch {
            departmentRepository.updateSubscribeStatus(selectedDepartment.name, true)
            departmentRepository.updateMainDepartmentStatus(selectedDepartment.name, true)
            departmentRepository.saveSubscribedDepartmentsToRemote(listOf(selectedDepartment))
        }
    }
}