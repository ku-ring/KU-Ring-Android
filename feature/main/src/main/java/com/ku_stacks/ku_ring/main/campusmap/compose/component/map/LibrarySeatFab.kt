package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun LibrarySeatFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(60),
        colors = ButtonDefaults.buttonColors(
            containerColor = KuringTheme.colors.background,
            contentColor = KuringTheme.colors.mainPrimary,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.2.dp,
            color = KuringTheme.colors.mainPrimary,
        ),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 14.dp,
        ),
        modifier = modifier.height(44.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_library_v2),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = stringResource(id = R.string.campus_library_seat_fab),
                style = KuringTheme.typography.body2,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun LibrarySeatFabPreview() {
    KuringTheme {
        LibrarySeatFab(onClick = {})
    }
}
