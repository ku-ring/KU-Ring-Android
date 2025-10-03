package com.ku_stacks.ku_ring.domain.academicevent.usecase

import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetDistinctAcademicEventUseCase @Inject constructor(
    private val academicEventRepository: AcademicEventRepository,
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<AcademicEvent>> =
        academicEventRepository.getAcademicEvents(startDate.toString(), endDate.toString())
}