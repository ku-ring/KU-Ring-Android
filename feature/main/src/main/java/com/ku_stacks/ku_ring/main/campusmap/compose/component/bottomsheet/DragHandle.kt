package com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@Composable
internal fun DragHandle(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 35.dp, height = 4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(KuringTheme.colors.gray200),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DragHandlePreview() {
    KuringTheme {
        Box(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(16.dp),
        ) {
            DragHandle()
        }
    }
}
