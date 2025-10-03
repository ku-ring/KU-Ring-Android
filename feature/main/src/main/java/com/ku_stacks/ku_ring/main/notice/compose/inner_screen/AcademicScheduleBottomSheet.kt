package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.HorizontalSlidingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.main.R.string.academic_event_bottom_sheet_cta
import com.ku_stacks.ku_ring.main.R.string.academic_event_bottom_sheet_title
import com.ku_stacks.ku_ring.main.calendar.type.ScheduleType
import com.ku_stacks.ku_ring.util.now
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AcademicEventBottomSheet(
    academicEvents: ImmutableList<AcademicEvent>,
    isVisible: Boolean,
    onNavigateToAcademicEvent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(isVisible) {
        if (isVisible) sheetState.show()
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { scope.launch { sheetState.hide() } },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            dragHandle = null,
            modifier = modifier
        ) {
            AcademicEventBottomSheetContent(
                academicEvents = academicEvents,
                onNavigateToAcademicEvent = {
                    onNavigateToAcademicEvent()
                    scope.launch { sheetState.hide() }
                },
            )
        }
    }
}

@Composable
private fun AcademicEventBottomSheetContent(
    academicEvents: ImmutableList<AcademicEvent>,
    onNavigateToAcademicEvent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isIndicatorVisible = academicEvents.size > 1

    Column(
        modifier = modifier
            .background(color = KuringTheme.colors.background)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val pagerState = rememberPagerState { academicEvents.size }

        Text(
            text = stringResource(academic_event_bottom_sheet_title),
            style = KuringTheme.typography.title2M,
            color = KuringTheme.colors.textTitle,
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            pageSpacing = 10.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.wrapContentHeight(),
        ) { page ->
            val academicEvent = academicEvents[page]
            Column(
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(
                        color = KuringTheme.colors.mainPrimarySelected,
                        shape = RoundedCornerShape(15.dp),
                    )
                    .padding(16.dp),
            ) {
                Text(
                    text = academicEvent.summary,
                    style = KuringTheme.typography.body1,
                    color = KuringTheme.colors.textTitle,
                    maxLines = 1,
                    modifier = Modifier.heightIn(min = 24.dp),
                )
                Text(
                    text = academicEvent.period,
                    style = KuringTheme.typography.tag2,
                    color = KuringTheme.colors.textCaption1,
                    maxLines = 1,
                    modifier = Modifier.heightIn(min = 18.dp),
                )
            }
        }

        if (isIndicatorVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalSlidingIndicator(
                pagerState = pagerState,
            )
        }

        KuringCallToAction(
            onClick = onNavigateToAcademicEvent,
            text = stringResource(academic_event_bottom_sheet_cta),
            modifier = Modifier.fillMaxWidth(),
            blur = false,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SingleAcademicEventBottomSheetPreview() {
    val events = listOf(
        AcademicEvent(
            id = 1,
            summary = "수강바구니 1차",
            category = ScheduleType.EVENT.name,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now(),
        )
    )

    KuringTheme {
        AcademicEventBottomSheetContent(
            academicEvents = events.toImmutableList(),
            onNavigateToAcademicEvent = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .width(390.dp)
                .wrapContentHeight(),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun MultipleAcademicEventBottomSheetPreview() {
    val events = buildList {
        repeat(5) { index ->
            add(
                AcademicEvent(
                    id = index.toLong(),
                    summary = "수강바구니 ${index}차",
                    category = ScheduleType.EVENT.name,
                    startDateTime = LocalDateTime.now(),
                    endDateTime = LocalDateTime.now(),
                )
            )
        }
    }
    KuringTheme {
        AcademicEventBottomSheetContent(
            academicEvents = events.toImmutableList(),
            onNavigateToAcademicEvent = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .width(390.dp)
                .wrapContentHeight(),
        )
    }
}