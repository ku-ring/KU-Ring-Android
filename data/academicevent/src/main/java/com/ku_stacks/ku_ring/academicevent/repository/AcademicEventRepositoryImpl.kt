package com.ku_stacks.ku_ring.academicevent.repository

import com.ku_stacks.ku_ring.academicevent.mapper.toDomain
import com.ku_stacks.ku_ring.academicevent.mapper.toEntity
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import com.ku_stacks.ku_ring.local.entity.AcademicEventEntity
import com.ku_stacks.ku_ring.local.room.AcademicEventDao
import com.ku_stacks.ku_ring.remote.academicevent.AcademicEventClient
import com.ku_stacks.ku_ring.util.IODispatcher
import com.ku_stacks.ku_ring.util.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AcademicEventRepositoryImpl @Inject constructor(
    private val academicEventDao: AcademicEventDao,
    private val academicEventClient: AcademicEventClient,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AcademicEventRepository {
    override suspend fun fetchAcademicEventsFromRemote(
        startDate: String?,
        endDate: String?,
    ): Result<Unit> = suspendRunCatching {
        val response = academicEventClient.fetchAcademicEvents(startDate, endDate)
        val eventEntities = response.data.toEntity()
        insertAcademicEventsIntoDB(eventEntities)
    }

    private suspend fun insertAcademicEventsIntoDB(eventEntities: List<AcademicEventEntity>) =
        withContext(ioDispatcher) {
            academicEventDao.insertAcademicEvents(eventEntities)
        }

    override suspend fun getAcademicEvents(
        startDate: String,
        endDate: String,
    ): Result<List<AcademicEvent>> = suspendRunCatching {
        val entities = getAcademicEventsFromDB(startDate, endDate)
        entities.map { it.toDomain() }
    }

    private suspend fun getAcademicEventsFromDB(
        startDate: String,
        endDate: String,
    ): List<AcademicEventEntity> = withContext(ioDispatcher) {
        academicEventDao.getAcademicEvents(startDate, endDate)
    }

    override fun getAcademicEventsAsFlow(
        startDate: String,
        endDate: String
    ): Flow<List<AcademicEvent>> =
        academicEventDao.getAcademicEventsAsFlow(startDate, endDate).map { eventEntities ->
            eventEntities.map { entity -> entity.toDomain() }
        }
}