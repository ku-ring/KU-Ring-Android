package com.ku_stacks.ku_ring.data.entity

import java.io.Serializable

data class Staff(
    val name: String,
    val major: String,
    val lab: String,
    val phone: String,
    val email: String,
    val department: String,
    val college: String
) : Serializable