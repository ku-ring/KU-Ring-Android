package com.ku_stacks.ku_ring.department

import com.ku_stacks.ku_ring.domain.Department

object UnsupportedDepartmentUtil {
    fun getUnsupportedDepartments(): List<Department> {
        return unsupportedDepartments
    }

    fun isUnsupportedDepartment(name: String?): Boolean {
        return unsupportedDepartments.any { it.name == name }
    }

    private val unsupportedDepartments = listOf(
        Department(
            name = "communication_design",
            shortName = "comdes",
            koreanName = "커뮤니케이션디자인학과",
            isSubscribed = false,
            isSelected = false,
            isNotificationEnabled = false,
        )
    )
}