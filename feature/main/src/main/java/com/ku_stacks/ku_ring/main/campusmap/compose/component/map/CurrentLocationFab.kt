package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun CurrentLocationFab(
    hasLocationPermission: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(KuringTheme.colors.background)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_crosshair_v2),
            contentDescription = stringResource(id = R.string.campus_map_current_location),
            tint = if (hasLocationPermission) {
                KuringTheme.colors.gray400
            } else {
                KuringTheme.colors.gray300
            },
            modifier = Modifier.size(26.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun CurrentLocationFabWithPermissionPreview() {
    KuringTheme {
        CurrentLocationFab(
            hasLocationPermission = true,
            onClick = {},
        )
    }
}

@LightAndDarkPreview
@Composable
private fun CurrentLocationFabWithoutPermissionPreview() {
    KuringTheme {
        CurrentLocationFab(
            hasLocationPermission = false,
            onClick = {},
        )
    }
}
