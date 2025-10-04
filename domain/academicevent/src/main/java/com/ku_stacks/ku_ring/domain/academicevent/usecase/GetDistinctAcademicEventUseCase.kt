package com.ku_stacks.ku_ring.domain.academicevent.usecase

import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetDistinctAcademicEventUseCase @Inject constructor(
    private val academicEventRepository: AcademicEventRepository,
) {
    operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<List<AcademicEvent>> =
        academicEventRepository.getAcademicEventsAsFlow(startDate.toString(), endDate.toString())
}