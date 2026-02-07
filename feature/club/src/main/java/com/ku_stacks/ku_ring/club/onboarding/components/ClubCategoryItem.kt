package com.ku_stacks.ku_ring.club.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme


@Composable
fun ClubCategoryItem(
    item: ClubCategoryItemState,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    val background by animateColorAsState(
        if (selected) KuringTheme.colors.mainPrimarySelected else KuringTheme.colors.background
    )
    val borderColor by animateColorAsState(
        if (selected) KuringTheme.colors.mainPrimary else KuringTheme.colors.gray200
    )
    Row(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .background(
                color = background,
                shape = shape,
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape,
            )
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = painterResource(item.iconId),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.height(IntrinsicSize.Min),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(item.categoryName),
                style = KuringTheme.typography.body2SB,
                color = KuringTheme.colors.textBody,
            )
            Text(
                text = stringResource(item.description),
                style = KuringTheme.typography.tag2,
                color = KuringTheme.colors.textCaption1,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun ClubCategoryItemPreview(
    @PreviewParameter(ClubCategoryItemPreviewParameterProvider::class) item: ClubCategoryItemState,
) {
    KuringTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(KuringTheme.colors.background),
        ) {
            ClubCategoryItem(
                item = item,
                selected = true,
                onClick = { },
                modifier = Modifier
                    .padding(20.dp),
            )
            ClubCategoryItem(
                item = item,
                selected = false,
                onClick = { },
                modifier = Modifier
                    .padding(20.dp),
            )
        }
    }
}