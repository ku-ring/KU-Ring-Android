package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.domain.Department
import io.reactivex.rxjava3.core.Single

interface SubscribeRepository {
    fun fetchSubscriptionFromRemote(token: String): Single<List<String>>
    fun saveSubscriptionToRemote(token: String, subscribeRequest: SubscribeRequest)
    suspend fun fetchSubscribedDepartments(): List<Department>
    suspend fun saveSubscribedDepartmentsToRemote(departments: List<Department>)
}