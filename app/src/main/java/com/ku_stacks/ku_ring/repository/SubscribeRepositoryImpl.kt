package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.DepartmentClient
import com.ku_stacks.ku_ring.data.mapper.toDepartment
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SubscribeRepositoryImpl @Inject constructor(
    private val departmentClient: DepartmentClient,
    private val pref: PreferenceUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SubscribeRepository {

    override suspend fun fetchSubscribedDepartments(): List<Department> {
        return try {
            pref.fcmToken?.let { fcmToken ->
                withContext(ioDispatcher) {
                    departmentClient.getSubscribedDepartments(fcmToken).data?.map {
                        it.toDepartment().copy(isSubscribed = true)
                    }
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    override suspend fun saveSubscribedDepartmentsToRemote(departments: List<Department>) {
        withContext(ioDispatcher) {
            pref.fcmToken?.let { fcmToken ->
                departmentClient.subscribeDepartments(fcmToken, departments.map { it.shortName })
            }
            Timber.d("Subscribed departments: ${departments.map { it.shortName }}")
        }
    }
}