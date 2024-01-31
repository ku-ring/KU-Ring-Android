package com.ku_stacks.ku_ring.edit_departments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.edit_departments.uimodel.DepartmentsUiModel
import com.ku_stacks.ku_ring.edit_departments.uimodel.PopupUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDepartmentsViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var popupUiModel by mutableStateOf<PopupUiModel?>(null)
        private set

    private val subscribedDepartments = MutableStateFlow<List<Department>>(emptyList())

    val departments: StateFlow<DepartmentsUiModel>
        get() = combine(
            snapshotFlow { query },
            subscribedDepartments,
        ) { query, subscribedDepartments ->
            getDepartmentsUiModel(query, subscribedDepartments)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = DepartmentsUiModel.SelectedDepartments(emptyList()),
        )

    init {
        collectSubscribedDepartments()
    }

    private fun collectSubscribedDepartments() {
        viewModelScope.launch {
            departmentRepository.getSubscribedDepartmentsAsFlow().collectLatest {
                subscribedDepartments.value = it
            }
        }
    }

    fun onDeleteAllButtonClick() {
        popupUiModel = PopupUiModel.DeleteAllPopupUiModel()
    }

    fun onQueryUpdate(newQuery: String) {
        query = newQuery
    }

    private suspend fun getDepartmentsUiModel(
        query: String,
        subscribedDepartments: List<Department>
    ): DepartmentsUiModel {
        return if (query.isEmpty()) {
            DepartmentsUiModel.SelectedDepartments(subscribedDepartments)
        } else {
            DepartmentsUiModel.SearchedDepartments(searchDepartments(query))
        }
    }

    private suspend fun getSubscribedDepartments(): List<Department> {
        return departmentRepository.getSubscribedDepartments()
    }

    private suspend fun searchDepartments(query: String): List<Department> {
        return departmentRepository.getDepartmentsByKoreanName(query)
    }

    fun onDeleteIconClick(department: Department) {
        popupUiModel = PopupUiModel.DeletePopupUiModel(department.name, department.koreanName)
    }

    fun onAddIconClick(department: Department) {
        popupUiModel = PopupUiModel.AddPopupUiModel(department.name, department.koreanName)
    }

    fun onCheckIconClick(department: Department) {
        onDeleteIconClick(department)
    }

    fun onPopupConfirmButtonClick(popupUiModel: PopupUiModel) {
        when (popupUiModel) {
            is PopupUiModel.AddPopupUiModel -> subscribeDepartment(popupUiModel.departmentName)
            is PopupUiModel.DeletePopupUiModel -> unsubscribeDepartment(popupUiModel.departmentName)
            is PopupUiModel.DeleteAllPopupUiModel -> onDeleteAll()
        }
        closePopup()
    }

    private fun subscribeDepartment(departmentKoreanName: String) {
        viewModelScope.launch {
            departmentRepository.updateSubscribeStatus(departmentKoreanName, true)
        }
    }

    private fun unsubscribeDepartment(departmentKoreanName: String) {
        viewModelScope.launch {
            departmentRepository.updateSubscribeStatus(departmentKoreanName, false)
        }
    }

    fun onDeleteAll() {
        viewModelScope.launch {
            departmentRepository.unsubscribeAllDepartments()
        }
    }

    fun onPopupDismissButtonClick() {
        onDismissPopup()
    }

    fun onDismissPopup() {
        closePopup()
    }

    private fun closePopup() {
        popupUiModel = null
    }
}