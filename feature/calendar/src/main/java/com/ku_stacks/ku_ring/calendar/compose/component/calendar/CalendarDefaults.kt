package com.ku_stacks.ku_ring.calendar.compose.component.calendar

import androidx.compose.runtime.Composable
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

internal object CalendarDefaults {
    const val YEAR_RANGE = 3

    @Composable
    fun calendarColor() = with(KuringTheme.colors) {
        CalendarColor(
            containerColor = background,
            contentColor = textTitle,
            selectedMarkerColor = mainPrimarySelected,
            selectedContentColor = mainPrimary,
            outDateContainerColor = background,
            outDateContentColor = textCaption2,
            todayContainerColor = background,
            todayContentColor = mainPrimary,
            headerContentColor = gray300,
            sundayHeaderContentColor = gray300,
        )
    }
}
