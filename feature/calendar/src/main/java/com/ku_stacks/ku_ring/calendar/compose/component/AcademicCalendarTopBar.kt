package com.ku_stacks.ku_ring.calendar.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.calendar.R.string.academic_calendar_top_bar_title
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun AcademicCalendarTopBar(
    modifier: Modifier = Modifier,
) {
    CenterTitleTopBar(
        title = stringResource(academic_calendar_top_bar_title),
        action = "",
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun AcademicCalendarTopBarPreview() {
    KuringTheme {
        AcademicCalendarTopBar()
    }
}