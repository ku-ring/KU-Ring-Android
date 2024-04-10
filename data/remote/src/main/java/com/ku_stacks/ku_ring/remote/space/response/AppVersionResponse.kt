package com.ku_stacks.ku_ring.remote.space.response

import com.google.gson.annotations.SerializedName

class AppVersionResponse(
    @SerializedName("minimum_version") val minimumAppVersion: String,
)