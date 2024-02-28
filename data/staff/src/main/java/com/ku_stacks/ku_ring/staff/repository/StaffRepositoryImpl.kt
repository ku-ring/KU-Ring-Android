package com.ku_stacks.ku_ring.staff.repository

import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.remote.staff.StaffClient
import com.ku_stacks.ku_ring.staff.mapper.toStaffList
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class StaffRepositoryImpl @Inject constructor(
    private val staffClient: StaffClient,
) : StaffRepository {
    override fun searchStaff(query: String): Single<List<Staff>> {
        return staffClient.fetchStaffList(query)
            .subscribeOn(Schedulers.io())
            .filter { it.isSuccess }
            .map { it.toStaffList() }
            .toSingle()
    }
}