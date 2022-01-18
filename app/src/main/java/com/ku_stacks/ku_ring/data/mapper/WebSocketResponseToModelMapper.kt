package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.entity.Staff
import com.ku_stacks.ku_ring.data.websocket.response.SearchStaffResponse

fun SearchStaffResponse.toStaff(): Staff {
    return Staff(
        name = name,
        major = major,
        lab = lab,
        phone = phone,
        email = email,
        department = department,
        college = college
    )
}