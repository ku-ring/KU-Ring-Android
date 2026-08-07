package com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.domain.Place
import com.ku_stacks.ku_ring.domain.PlaceFacility
import com.ku_stacks.ku_ring.domain.PlaceOperationHours
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.campusmap.compose.preview.CampusMapPlacePreviewParameterProvider
import com.ku_stacks.ku_ring.main.campusmap.type.CampusMapCategory

internal val CampusMapCollapsedSheetHeight = 318.dp

@Composable
internal fun CampusMapDetailSheetHost(
    place: Place,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable(place.id) { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        CampusMapBottomSheet(
            place = place,
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onDismiss = onDismiss,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
internal fun CampusMapBottomSheet(
    place: Place,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CampusMapDraggableBottomSheet(
        isExpanded = isExpanded,
        collapsedHeight = CampusMapCollapsedSheetHeight,
        onExpandedChange = onExpandedChange,
        onDismiss = onDismiss,
        modifier = modifier.fillMaxWidth(),
    ) { scrollModifier ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .then(scrollModifier)
                .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            CampusMapPlaceSummary(
                place = place,
                onDismiss = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = KuringTheme.colors.borderline)
            Spacer(modifier = Modifier.height(20.dp))

            CampusMapFacilityDetails(
                facilities = place.facilities,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun CampusMapPlaceSummary(
    place: Place,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PlaceHeader(
            place = place,
            onDismiss = onDismiss,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlaceTopInfo(
            place = place,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PlaceHeader(
    place: Place,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.height(60.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(end = 32.dp),
        ) {
            Text(
                text = place.name,
                style = KuringTheme.typography.noticeTitle,
                color = KuringTheme.colors.textTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = place.category.orDash(),
                    style = KuringTheme.typography.caption1_1,
                    color = KuringTheme.colors.textCaption1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    place.facilities
                        .distinctBy { it.category }
                        .forEach { facility ->
                            CampusMapFacilityIcon(
                                category = facility.category,
                                tint = KuringTheme.colors.textBody,
                            )
                        }
                }
            }
        }

        CloseCircleButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 12.dp, y = (-7).dp),
        )
    }
}

@Composable
private fun PlaceTopInfo(
    place: Place,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            AddressRow(
                label = stringResource(id = R.string.campus_map_address),
                value = place.address.orDash(),
                onCopyClick = {
                    clipboardManager.setText(AnnotatedString(place.address))
                },
            )

            CampusMapOperationHours(operationHours = place.operationHours)
        }

        Spacer(modifier = Modifier.width(10.dp))

        PlaceImage(
            imageUrl = place.imageUrl,
            modifier = Modifier.size(80.dp),
        )
    }
}

@Composable
private fun AddressRow(
    label: String,
    value: String,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CampusMapDetailRow(
        label = label,
        value = value,
        trailingContent = {
            CopyAddressButton(onClick = onCopyClick)
        },
        modifier = modifier,
    )
}

@Composable
private fun PlaceImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFD9D9D9)),
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun CloseCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(KuringTheme.colors.gray300),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_x_v2),
                contentDescription = stringResource(id = R.string.campus_map_close_place_detail),
                tint = KuringTheme.colors.background,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

@Composable
private fun CopyAddressButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_campus_map_copy),
            contentDescription = stringResource(id = R.string.campus_map_copy_address),
            tint = KuringTheme.colors.gray300,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun CampusMapFacilityDetails(
    facilities: List<PlaceFacility>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.campus_map_facility_detail_title),
            style = KuringTheme.typography.caption1_1,
            color = KuringTheme.colors.textTitle,
        )

        if (facilities.isEmpty()) {
            Text(
                text = stringResource(id = R.string.campus_map_empty_facility),
                style = KuringTheme.typography.caption1,
                color = KuringTheme.colors.textCaption1,
            )
        } else {
            facilities.forEach { facility ->
                FacilityInfo(facility = facility)
            }
        }
    }
}

@Composable
private fun FacilityInfo(
    facility: PlaceFacility,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            CampusMapFacilityIcon(
                category = facility.category,
                tint = KuringTheme.colors.mainPrimary,
            )

            Text(
                text = facility.name,
                style = KuringTheme.typography.caption1_1,
                color = KuringTheme.colors.textBody,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }

        CampusMapDetailRow(
            label = stringResource(id = R.string.campus_map_location),
            value = facility.location.orDash(),
        )

        CampusMapOperationHours(operationHours = facility.operationHours)
    }
}

@Composable
private fun CampusMapOperationHours(
    operationHours: PlaceOperationHours?,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        CampusMapDetailRow(
            label = stringResource(id = R.string.campus_map_operation_hours),
            value = operationHours?.current.orDash(),
        )
        CampusMapPeriodOperationHoursRow(
            label = stringResource(id = R.string.campus_map_semester),
            weekdayHours = operationHours?.semesterWeekday,
            weekendHours = operationHours?.semesterWeekend,
            labelWidth = 72.dp,
            labelStyle = KuringTheme.typography.caption1_1,
            labelColor = KuringTheme.colors.textBody,
            valueStyle = KuringTheme.typography.caption1_1,
            modifier = Modifier.padding(start = 4.dp),
        )
        CampusMapPeriodOperationHoursRow(
            label = stringResource(id = R.string.campus_map_vacation),
            weekdayHours = operationHours?.vacationWeekday,
            weekendHours = operationHours?.vacationWeekend,
            labelWidth = 72.dp,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun CampusMapPeriodOperationHoursRow(
    label: String,
    weekdayHours: String?,
    weekendHours: String?,
    modifier: Modifier = Modifier,
    labelWidth: Dp = 76.dp,
    labelStyle: TextStyle = KuringTheme.typography.caption1,
    labelColor: Color = KuringTheme.colors.textCaption1,
    valueStyle: TextStyle = KuringTheme.typography.caption1,
    valueColor: Color = KuringTheme.colors.textBody,
) {
    val unavailable = stringResource(id = R.string.campus_map_operation_hours_unavailable)
    val weekday = stringResource(
        id = R.string.campus_map_weekday_operation_hours,
        weekdayHours ?: unavailable,
    )
    val weekend = stringResource(
        id = R.string.campus_map_weekend_operation_hours,
        weekendHours ?: unavailable,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor,
            maxLines = 1,
            modifier = Modifier.width(labelWidth),
        )

        Column(
            modifier = Modifier.weight(1f, fill = false),
        ) {
            Text(
                text = weekday,
                style = valueStyle,
                color = valueColor,
            )
            Text(
                text = weekend,
                style = valueStyle,
                color = valueColor,
            )
        }
    }
}

@Composable
private fun CampusMapDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelWidth: Dp = 76.dp,
    labelStyle: TextStyle = KuringTheme.typography.caption1,
    labelColor: Color = KuringTheme.colors.textCaption1,
    valueStyle: TextStyle = KuringTheme.typography.caption1,
    valueColor: Color = KuringTheme.colors.textBody,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor,
            maxLines = 1,
            modifier = Modifier.width(labelWidth),
        )

        Text(
            text = value,
            style = valueStyle,
            color = valueColor,
            modifier = Modifier.weight(1f, fill = false),
        )

        trailingContent?.invoke()
    }
}

@Composable
private fun CampusMapFacilityIcon(
    category: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(KuringTheme.colors.gray100),
    ) {
        Icon(
            painter = painterResource(id = CampusMapCategory.iconRes(category)),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp),
        )
    }
}

private fun String?.orDash(): String =
    takeUnless { it.isNullOrBlank() } ?: "-"

@LightAndDarkPreview
@Composable
private fun CampusMapBottomSheetExpandedPreview(
    @PreviewParameter(CampusMapPlacePreviewParameterProvider::class) place: Place,
) {
    KuringTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE9EEF2)),
        ) {
            CampusMapBottomSheet(
                place = place,
                isExpanded = true,
                onExpandedChange = {},
                onDismiss = {},
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun CampusMapBottomSheetCollapsedPreview(
    @PreviewParameter(CampusMapPlacePreviewParameterProvider::class) place: Place,
) {
    KuringTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE9EEF2)),
        ) {
            CampusMapBottomSheet(
                place = place,
                isExpanded = false,
                onExpandedChange = {},
                onDismiss = {},
            )
        }
    }
}
