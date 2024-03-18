package com.ku_stacks.ku_ring.edit_subscription.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringThemeTest
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.edit_subscription.R
import com.ku_stacks.ku_ring.edit_subscription.uimodel.DepartmentSubscriptionUiModel


@Composable
internal fun DepartmentSubscriptionItem(
    uiModel: DepartmentSubscriptionUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconId =
        if (uiModel.isSelected) R.drawable.ic_check_checked else R.drawable.ic_check_unchecked

    val onClickLabel = getOnClickLabel(isSelected = uiModel.isSelected)
    val description = getDescription(isSelected = uiModel.isSelected)

    Row(
        modifier = modifier
            .clickable(onClick = onClick, onClickLabel = onClickLabel)
            .padding(horizontal = 36.dp, vertical = 16.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = description
            },
    ) {
        Text(
            text = uiModel.name,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textBody,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
        )
    }
}

@Composable
private fun getOnClickLabel(isSelected: Boolean): String {
    val onClickLabelId =
        if (isSelected) R.string.department_subscription_unsubscribe else R.string.department_subscription_subscribe
    return stringResource(id = onClickLabelId)
}

@Composable
private fun getDescription(isSelected: Boolean): String {
    val descriptionId =
        if (isSelected) R.string.department_subscription_item_subscribed else R.string.department_subscription_item_unsubscribed
    return stringResource(id = descriptionId)
}

@LightAndDarkPreview
@Composable
private fun DepartmentSubscriptionItemPreview() {
    var isSelected by remember { mutableStateOf(false) }
    KuringThemeTest {
        DepartmentSubscriptionItem(
            uiModel = DepartmentSubscriptionUiModel("스마트ICT융합공학과", isSelected),
            onClick = { isSelected = !isSelected },
            modifier = Modifier
                .fillMaxWidth()
                .background(KuringTheme.colors.background),
        )
    }
}