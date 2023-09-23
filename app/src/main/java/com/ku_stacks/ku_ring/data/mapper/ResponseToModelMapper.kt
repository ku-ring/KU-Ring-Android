package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.DepartmentResponse
import com.ku_stacks.ku_ring.data.api.response.SearchStaffListResponse
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Staff

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

fun DepartmentResponse.toDepartment() = Department(
    name = name!!,
    shortName = shortName!!,
    koreanName = this.korName!!,
    isSubscribed = false,
    isSelected = false,
    isNotificationEnabled = false
)