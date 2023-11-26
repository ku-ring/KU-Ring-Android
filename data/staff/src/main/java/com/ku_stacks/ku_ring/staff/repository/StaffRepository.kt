package com.ku_stacks.ku_ring.staff.repository

import com.ku_stacks.ku_ring.domain.Staff
import io.reactivex.rxjava3.core.Single

interface StaffRepository {
    fun searchStaff(query: String): Single<List<Staff>>
}