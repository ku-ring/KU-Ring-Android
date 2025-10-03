package com.ku_stacks.ku_ring.academicevent

import com.ku_stacks.ku_ring.remote.academicevent.response.AcademicEventListResponse
import com.ku_stacks.ku_ring.remote.academicevent.response.AcademicEventResponse

object AcademicEventTestUtil {
    fun mockAcademicEventListResponse() = AcademicEventListResponse(
        code = 200,
        message = "조회되었습니다.",
        data = listOf(
            AcademicEventResponse(
                id = 2417,
                eventUid = "EC66FB8098",
                summary = "등록금의 6분의 5해당액 반환",
                description = null,
                category = "ETC",
                startTime = "2025-03-04T00:00:00",
                endTime = "2025-04-03T00:00:00"
            ),
            AcademicEventResponse(
                id = 2429,
                eventUid = "41C75146F2",
                summary = "제적생변동상황 통계작업(휴복학처리불가)",
                description = null,
                category = "ETC",
                startTime = "2025-03-31T00:00:00",
                endTime = "2025-04-12T00:00:00"
            ),
        )
    )
}