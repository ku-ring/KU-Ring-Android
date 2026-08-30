package com.ku_stacks.ku_ring.remote.user.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcademicEventNotificationRequest(
    @SerialName("enabled") val enabled: Boolean,
)
