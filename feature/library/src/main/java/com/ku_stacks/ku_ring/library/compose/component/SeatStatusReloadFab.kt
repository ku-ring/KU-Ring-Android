package com.ku_stacks.ku_ring.library.compose.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.library.R

@Composable
internal fun SeatStatusReloadFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = KuringTheme.colors.background,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp),
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_refresh),
                contentDescription = null,
                modifier = Modifier.padding(11.dp),
                tint = KuringTheme.colors.gray300
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun SeatStatusReloadFabPreview() {
    KuringTheme {
        SeatStatusReloadFab(onClick = { })
    }
}