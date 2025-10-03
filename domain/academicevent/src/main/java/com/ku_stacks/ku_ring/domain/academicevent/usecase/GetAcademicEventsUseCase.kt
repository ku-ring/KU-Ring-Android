package com.ku_stacks.ku_ring.domain.academicevent.usecase

import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class GetAcademicEventsUseCase @Inject constructor(
    private val academicEventRepository: AcademicEventRepository,
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<Map<String, List<AcademicEvent>>> = academicEventRepository
        .getAcademicEvents(startDate.toString(), endDate.toString())
        .mapByDate(startDate, endDate)

    private fun Result<List<AcademicEvent>>.mapByDate(
        startDate: LocalDate,
        endDate: LocalDate,
    ) = map { academicEvents ->
        buildMap {
            academicEvents.forEach { event ->
                val startRange = event.startDateTime.date.coerceAtLeast(startDate)
                val endRange = event.endDateTime.date.coerceAtMost(endDate)

                // 특정 학사 일정의 기간 내 모든 날짜에 매핑되어야 한다.
                for (date in startRange..endRange) {
                    val key = date.toString()
                    getOrPut(key) { mutableListOf() }.add(event)
                }
            }
        }
    }
}