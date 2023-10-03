package com.ku_stacks.ku_ring.domain

data class Department(
    val name: String,
    val shortName: String,
    val koreanName: String,
    val isSubscribed: Boolean,
    val isSelected: Boolean,
    val isNotificationEnabled: Boolean,
) {
    companion object {
        fun mock() = Department(
            name = "smart_ict_convergence",
            shortName = "sicte",
            koreanName = "스마트ICT융합공학과",
            isSubscribed = false,
            isSelected = false,
            isNotificationEnabled = false,
        )
    }
}