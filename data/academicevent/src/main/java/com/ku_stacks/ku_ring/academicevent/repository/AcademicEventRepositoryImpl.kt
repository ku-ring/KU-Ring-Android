package com.ku_stacks.ku_ring.academicevent.repository

import com.ku_stacks.ku_ring.academicevent.mapper.toDomain
import com.ku_stacks.ku_ring.academicevent.mapper.toEntity
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.local.entity.AcademicEventEntity
import com.ku_stacks.ku_ring.local.room.AcademicEventDao
import com.ku_stacks.ku_ring.remote.academicevent.AcademicEventClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AcademicEventRepositoryImpl @Inject constructor(
    private val academicEventDao: AcademicEventDao,
    private val academicEventClient: AcademicEventClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AcademicEventRepository {
    override suspend fun fetchAcademicEventsFromRemote(
        startDate: String?,
        endDate: String?,
    ) {
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
    ): List<AcademicEvent> {
        val entities = getAcademicEventsFromDB(startDate, endDate)
        return entities.map { it.toDomain() }
    }

    private suspend fun getAcademicEventsFromDB(
        startDate: String,
        endDate: String,
    ): List<AcademicEventEntity> = withContext(ioDispatcher) {
        academicEventDao.getAcademicEvents(startDate, endDate)
    }
}