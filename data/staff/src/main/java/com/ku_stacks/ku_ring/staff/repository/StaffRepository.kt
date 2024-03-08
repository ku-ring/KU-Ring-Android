package com.ku_stacks.ku_ring.staff.repository

import com.ku_stacks.ku_ring.domain.Staff

interface StaffRepository {
    suspend fun searchStaff(query: String): List<Staff>
}
