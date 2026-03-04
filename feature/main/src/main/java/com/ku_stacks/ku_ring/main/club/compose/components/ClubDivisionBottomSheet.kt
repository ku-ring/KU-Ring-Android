package com.ku_stacks.ku_ring.main.club.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.utils.ensureLineHeight
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.main.R.string.club_bottom_sheet_division_confirm
import com.ku_stacks.ku_ring.main.R.string.club_bottom_sheet_division_reset
import com.ku_stacks.ku_ring.main.R.string.club_bottom_sheet_division_title
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubDivisionBottomSheet(
    selectedItems: Set<ClubDivision>,
    onConfirm: (Set<ClubDivision>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var currentSelected by remember { mutableStateOf(selectedItems) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null,
        containerColor = KuringTheme.colors.background,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp),
        ) {
            Text(
                text = stringResource(club_bottom_sheet_division_title),
                style = KuringTheme.typography.title2.ensureLineHeight(),
                color = KuringTheme.colors.textBody,
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            ClubDivisionItemRow(
                selectedItems = currentSelected,
                onItemClick = { item ->
                    currentSelected = if (currentSelected.contains(item)) {
                        currentSelected - item
                    } else {
                        currentSelected + item
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ActionButtonGroup(
            selectedItemCount = currentSelected.size,
            onConfirm = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onConfirm(currentSelected.toSet())
                        onDismissRequest()
                    }
                }
            },
            onReset = { currentSelected = emptySet() },
            confirmEnabled = currentSelected != selectedItems,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ClubDivisionItemRow(
    selectedItems: Set<ClubDivision>,
    onItemClick: (ClubDivision) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ClubDivision.entries.forEach { item ->
            ClubDivisionChipButton(
                item = item,
                isSelected = item in selectedItems,
                onClick = onItemClick,
            )
        }
    }
}

@Composable
private fun ActionButtonGroup(
    selectedItemCount: Int,
    confirmEnabled: Boolean,
    onConfirm: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onReset,
            shape = RoundedCornerShape(100),
            border = BorderStroke(1.dp, KuringTheme.colors.gray200),
            contentPadding = PaddingValues(
                horizontal = 32.dp,
                vertical = 16.dp,
            ),
        ) {
            Text(
                text = stringResource(club_bottom_sheet_division_reset),
                style = KuringTheme.typography.body2SB,
                color = KuringTheme.colors.textCaption1,
            )
        }

        KuringCallToAction(
            text = stringResource(club_bottom_sheet_division_confirm, selectedItemCount),
            onClick = onConfirm,
            enabled = confirmEnabled,
            modifier = Modifier.weight(1f),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun ClubDivisionBottomSheetPreview() {
    KuringTheme {
        ClubDivisionBottomSheet(
            selectedItems = setOf(),
            onConfirm = {},
            onDismissRequest = {},
        )
    }
}
