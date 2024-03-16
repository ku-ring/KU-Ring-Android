package com.ku_stacks.ku_ring.ui_util.preview_data

import com.ku_stacks.ku_ring.domain.Department

val previewDepartments = (1..10).map {
    Department(
        name = "department$it",
        shortName = "dept$it",
        koreanName = "학과 $it".repeat(if (it < 8) 1 else 5),
        isSubscribed = false,
        isSelected = false,
        isNotificationEnabled = false,
    )
}