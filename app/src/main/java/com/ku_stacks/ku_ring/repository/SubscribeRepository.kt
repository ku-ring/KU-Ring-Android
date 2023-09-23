package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.domain.Department

interface SubscribeRepository {
    suspend fun fetchSubscribedDepartments(): List<Department>
    suspend fun saveSubscribedDepartmentsToRemote(departments: List<Department>)
}