package com.ku_stacks.ku_ring.main.setting.compose.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun ChevronIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.ic_chevron_v2),
        contentDescription = null,
        modifier = modifier.size(20.dp),
        tint = KuringTheme.colors.gray300,
    )
}