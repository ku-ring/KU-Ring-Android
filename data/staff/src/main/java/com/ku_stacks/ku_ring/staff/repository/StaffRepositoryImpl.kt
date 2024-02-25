package com.ku_stacks.ku_ring.staff.repository

import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.remote.staff.StaffClient
import com.ku_stacks.ku_ring.staff.mapper.toStaffList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StaffRepositoryImpl @Inject constructor(
    private val staffClient: StaffClient,
) : StaffRepository {
    override suspend fun searchStaff(query: String): List<Staff> = withContext(Dispatchers.IO) {
        staffClient.fetchStaffList(query)
            .takeIf { it.isSuccess }?.toStaffList() ?: emptyList()
    }
}
