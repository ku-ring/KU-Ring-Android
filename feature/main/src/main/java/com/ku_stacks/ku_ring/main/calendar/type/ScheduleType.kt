package com.ku_stacks.ku_ring.main.calendar.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme


enum class ScheduleType {
    ACADEMIC_DEGREE, REGISTRATION_COURSE_GRADE, ACADEMIC_OPERATION_EVENT, ETC;

    companion object {
        fun from(value: String): ScheduleType = when(value.uppercase()) {
            "ACADEMIC_DEGREE" -> ACADEMIC_DEGREE
            "REGISTRATION_COURSE_GRADE" -> REGISTRATION_COURSE_GRADE
            "ACADEMIC_OPERATION_EVENT" -> ACADEMIC_OPERATION_EVENT
            else -> ETC
        }
    }
}

@Composable
fun ScheduleType.color(): Color = when(this) {
    ScheduleType.ACADEMIC_DEGREE -> KuringTheme.colors.degree
    ScheduleType.REGISTRATION_COURSE_GRADE -> KuringTheme.colors.registration
    ScheduleType.ACADEMIC_OPERATION_EVENT -> KuringTheme.colors.event
    ScheduleType.ETC -> KuringTheme.colors.etc
}