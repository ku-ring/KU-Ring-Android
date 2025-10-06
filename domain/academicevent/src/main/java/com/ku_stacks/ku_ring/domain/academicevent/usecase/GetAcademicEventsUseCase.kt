package com.ku_stacks.ku_ring.domain.academicevent.usecase

import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
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

    /*private fun Result<List<AcademicEvent>>.mapByDate(
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
    }*/

    private fun Result<List<AcademicEvent>>.mapByDate(
        startDate: LocalDate,
        endDate: LocalDate,
    ) = map { academicEvents ->
        academicEvents.flatMap { event ->
            val from = maxOf(event.startDateTime.date, startDate)
            val to = minOf(event.endDateTime.date, endDate)
            if (from > to) emptyList()
            else generateSequence(from) {
                it.plus(1, DateTimeUnit.DAY).takeIf { date -> date <= to }
            }.map { it.toString() to event }.toList()
        }.groupBy({ it.first }, { it.second })
    }
}