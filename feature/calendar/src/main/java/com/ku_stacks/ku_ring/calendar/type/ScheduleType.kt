package com.ku_stacks.ku_ring.calendar.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ku_stacks.ku_ring.calendar.type.ScheduleType.DEGREE
import com.ku_stacks.ku_ring.calendar.type.ScheduleType.ETC
import com.ku_stacks.ku_ring.calendar.type.ScheduleType.EVENT
import com.ku_stacks.ku_ring.calendar.type.ScheduleType.REGISTRATION
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
    DEGREE -> KuringTheme.colors.degree
    REGISTRATION -> KuringTheme.colors.registration
    EVENT -> KuringTheme.colors.event
    ETC -> KuringTheme.colors.etc
}