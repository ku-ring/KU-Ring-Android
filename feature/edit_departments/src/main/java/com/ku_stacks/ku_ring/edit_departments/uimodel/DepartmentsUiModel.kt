package com.ku_stacks.ku_ring.edit_departments.uimodel

import com.ku_stacks.ku_ring.domain.Department

/**
 * 학과 편집 화면에서 학과를 보여주는 경우를 wrap하는 클래스이다.
 *
 * @param departments 화면에 보여줄 학과 리스트이다. 보여주는 학과의 의미는 상황에 따라 다를 수 있다.
 */
sealed class DepartmentsUiModel(val departments: List<Department>) {
    class SelectedDepartments(departments: List<Department>) : DepartmentsUiModel(departments)
    class SearchedDepartments(departments: List<Department>) : DepartmentsUiModel(departments)
}