package com.ku_stacks.ku_ring.domain

data class Department(
    val name: String,
    val shortName: String,
    val koreanName: String,
    val isSubscribed: Boolean,
    val isSelected: Boolean,
    val isNotificationEnabled: Boolean,
)