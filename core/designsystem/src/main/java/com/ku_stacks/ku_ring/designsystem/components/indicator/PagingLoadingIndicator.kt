package com.ku_stacks.ku_ring.designsystem.components.indicator

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.material.CircularProgressIndicator
import com.ku_stacks.ku_ring.designsystem.R

@Composable
fun PagingLoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.kus_green),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}