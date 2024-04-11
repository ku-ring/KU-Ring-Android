package com.ku_stacks.ku_ring.edit_subscription.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.values.Pretendard
import com.ku_stacks.ku_ring.edit_subscription.R
import com.ku_stacks.ku_ring.edit_subscription.uimodel.NormalSubscriptionUiModel

@Composable
internal fun NormalSubscriptionItem(
    uiModel: NormalSubscriptionUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NormalSubscriptionItem(
        categoryIconId = uiModel.categoryIconId,
        categoryName = uiModel.categoryName,
        isSelected = uiModel.isSelected,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun NormalSubscriptionItem(
    @DrawableRes categoryIconId: Int,
    categoryName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            KuringTheme.colors.mainPrimary.copy(alpha = 0.1f)
        } else {
            KuringTheme.colors.textBody.copy(alpha = 0.03f)
        },
        animationSpec = tween(),
        label = "background color",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) KuringTheme.colors.mainPrimary else Color.Transparent,
        animationSpec = tween(),
        label = "border color",
    )

    val shape = RoundedCornerShape(8.dp)
    val description = stringResource(id = R.string.normal_subscription_description, categoryName)
    Column(
        modifier = modifier
            .clip(shape)
            .aspectRatio(1f)
            .background(color = backgroundColor)
            .border(width = 2.dp, color = borderColor, shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clearAndSetSemantics {
                contentDescription = description
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = categoryIconId),
            contentDescription = null,
        )
        Crossfade(isSelected, label = "norman item") { isSelected ->
            Text(
                text = categoryName,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = if (isSelected) FontWeight(600) else FontWeight(500),
                    color = KuringTheme.colors.textBody,
                )
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun NormalSubscriptionItemPreview() {
    // Android Studio에서는 아이템을 클릭해도 배경색이 바뀌지 않는다. (프리뷰 오류)
    // 프리뷰를 에뮬레이터에서 실행하면 배경색이 정상적으로 바뀐다.
    var isSelected by remember { mutableStateOf(false) }

    KuringTheme {
        Column {
            NormalSubscriptionItem(
                categoryIconId = R.drawable.bachelor,
                categoryName = "학사",
                isSelected = isSelected,
                onClick = { isSelected = !isSelected },
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .padding(10.dp)
                    .size(96.dp)
            )
        }
    }
}