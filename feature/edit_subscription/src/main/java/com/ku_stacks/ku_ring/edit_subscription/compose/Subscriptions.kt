package com.ku_stacks.ku_ring.edit_subscription.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_subscription.EditSubscriptionTab
import com.ku_stacks.ku_ring.edit_subscription.uimodel.DepartmentSubscriptionUiModel
import com.ku_stacks.ku_ring.edit_subscription.uimodel.NormalSubscriptionUiModel

@Composable
fun Subscriptions(
    selectedTab: EditSubscriptionTab,
    categories: List<NormalSubscriptionUiModel>,
    departments: List<DepartmentSubscriptionUiModel>,
    onCategoryClick: (Int) -> Unit,
    onDepartmentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

    }
}

@Preview
@Composable
private fun SubscriptionsPreview() {
    val categories =
        NormalSubscriptionUiModel.initialValues.mapIndexed { index, normalSubscriptionUiModel ->
            normalSubscriptionUiModel.copy(isSelected = index < 3)
        }
    val departments = listOf("컴퓨터공학부", "스마트ICT융합공학과", "전기전자공학부").mapIndexed { index, s ->
        DepartmentSubscriptionUiModel(s, index == 0)
    }
    KuringTheme {
        Subscriptions(
            selectedTab = EditSubscriptionTab.NORMAL,
            categories = categories,
            departments = departments,
            onCategoryClick = {},
            onDepartmentClick = {},
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}