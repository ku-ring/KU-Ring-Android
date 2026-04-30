package com.ku_stacks.ku_ring.remote.space.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AppVersionResponse(
    @SerialName("minimum_version") val minimumAppVersion: String,
)