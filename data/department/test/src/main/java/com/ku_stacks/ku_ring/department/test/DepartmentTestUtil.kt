package com.ku_stacks.ku_ring.department.test

import com.ku_stacks.ku_ring.domain.Department

object DepartmentTestUtil {
    fun fakeDepartment(
        name: String = "smart_ict_convergence",
        shortName: String = "sicte",
        koreanName: String = "스마트ICT융합공학과",
        isSubscribed: Boolean = false,
        isSelected: Boolean = false,
        isNotificationEnabled: Boolean = false,
    ) = Department(
        name = name,
        shortName = shortName,
        koreanName = koreanName,
        isSubscribed = isSubscribed,
        isSelected = isSelected,
        isNotificationEnabled = isNotificationEnabled,
    )
}