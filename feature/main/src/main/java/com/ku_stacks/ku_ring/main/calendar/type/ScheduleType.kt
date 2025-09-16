package com.ku_stacks.ku_ring.main.calendar.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme


enum class ScheduleType {
    DEGREE, REGISTRATION, EVENT, ETC;

    companion object {
        fun from(value: String): ScheduleType = when(value.uppercase()) {
            "DEGREE" -> DEGREE
            "REGISTRATION" -> REGISTRATION
            "EVENT" -> EVENT
            else -> ETC
        }
    }
}

@Composable
fun ScheduleType.color(): Color = when(this) {
    ScheduleType.DEGREE -> KuringTheme.colors.degree
    ScheduleType.REGISTRATION -> KuringTheme.colors.registration
    ScheduleType.EVENT -> KuringTheme.colors.event
    ScheduleType.ETC -> KuringTheme.colors.etc
}