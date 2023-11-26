package com.ku_stacks.ku_ring.staff.mapper

import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.remote.staff.response.SearchStaffListResponse

fun SearchStaffListResponse.toStaffList(): List<Staff> {
    return data?.staffList?.map {
        Staff(
            name = it.name,
            major = it.major,
            lab = it.lab,
            phone = it.phone,
            email = it.email,
            department = it.deptName,
            college = it.collegeName,
        )
    } ?: emptyList()
}