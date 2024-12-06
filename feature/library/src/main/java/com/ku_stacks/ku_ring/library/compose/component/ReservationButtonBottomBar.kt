package com.ku_stacks.ku_ring.library.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.library.R

@Composable
internal fun ReservationButtonBottomBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KuringCallToAction(
        text = stringResource(R.string.library_seats_button_reservation),
        modifier = modifier
            .background(color = KuringTheme.colors.background)
            .navigationBarsPadding()
            .fillMaxWidth(),
        onClick = onClick,
        enabled = true,
        blur = true
    )
}

@LightAndDarkPreview
@Composable
private fun ReservationButtonBottomBarPreview() {
    KuringTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(top = 30.dp),
        ) {
            ReservationButtonBottomBar(
                onClick = {},
                modifier = Modifier
                    .background(KuringTheme.colors.background)
            )
        }
    }
}