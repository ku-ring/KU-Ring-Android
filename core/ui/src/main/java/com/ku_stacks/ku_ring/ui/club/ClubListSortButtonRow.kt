package com.ku_stacks.ku_ring.ui.club

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
fun ClubListSortButtonRow(
    selectedOption: ClubSortOption,
    onOptionSelect: (ClubSortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClubSortOption.entries.forEach { option ->
            val isSelected = selectedOption == option
            SortOptionButton(
                sortOption = option,
                isSelected = isSelected,
                onClick = onOptionSelect,
            )

            if (option != ClubSortOption.entries.last()) {
                VerticalDivider(
                    thickness = 1.dp,
                    color = KuringTheme.colors.gray200,
                    modifier = Modifier
                        .height(16.dp)
                        .padding(horizontal = 5.dp),
                )
            }
        }
    }
}

@Composable
private fun SortOptionButton(
    sortOption: ClubSortOption,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (ClubSortOption) -> Unit,
) {
    val textColor = with(KuringTheme.colors) {
        if (isSelected) textCaption1 else textCaption2
    }
    val textStyle = with(KuringTheme.typography) {
        if (isSelected) caption1_1 else caption1
    }
    val interactionSource = remember { MutableInteractionSource()  }

    Text(
        text = sortOption.text,
        style = textStyle,
        color = textColor,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { onClick(sortOption) }
            .padding(horizontal = 4.dp, vertical = 5.dp),
    )
}

@LightAndDarkPreview
@Composable
private fun ClubListSortButtonRowPreview() {
    var selectedOption by remember { mutableStateOf(ClubSortOption.END_OF_RECRUITMENT) }

    KuringTheme {
        ClubListSortButtonRow(
            selectedOption = selectedOption,
            onOptionSelect = { selectedOption = it},
            modifier = Modifier.background(KuringTheme.colors.background),
        )
    }
}
