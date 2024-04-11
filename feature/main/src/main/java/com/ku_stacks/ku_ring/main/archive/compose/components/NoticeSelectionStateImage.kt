package com.ku_stacks.ku_ring.main.archive.compose.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun NoticeSelectionStateImage(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = isSelected,
        label = "notice selection icon",
        modifier = modifier,
    ) {
        val imageId =
            if (it) R.drawable.ic_check_circle_fill_v2 else R.drawable.ic_check_circle_fill_2_v2
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun NoticeSelectionStateImagePreview() {
    KuringTheme {
        NoticeSelectionStateImage(
            isSelected = false,
            modifier = Modifier.padding(16.dp),
        )
    }
}