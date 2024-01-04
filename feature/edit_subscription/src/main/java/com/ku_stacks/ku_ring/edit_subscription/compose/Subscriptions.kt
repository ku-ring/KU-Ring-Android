package com.ku_stacks.ku_ring.edit_subscription.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.edit_subscription.SubscriptionUiModel

@Composable
fun Subscriptions(
    categories: List<SubscriptionUiModel>,
    categoriesHeaderTitle: String,
    departments: List<SubscriptionUiModel>,
    departmentsHeaderTitle: String,
    onItemClick: (SubscriptionUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

    }
}

@Preview
@Composable
private fun SubscriptionsPreview() {
    val categories =
        listOf("학사", "장학", "취창업", "국제", "학생", "산학", "일반", "도서관").mapIndexed { index, s ->
            SubscriptionUiModel(s, index < 3)
        }
    val departments = listOf("컴퓨터공학부", "스마트ICT융합공학과", "전기전자공학부").mapIndexed { index, s ->
        SubscriptionUiModel(s, index == 0)
    }
    KuringTheme {
        Subscriptions(
            categories = categories,
            categoriesHeaderTitle = "대학 공지 카테고리",
            departments = departments,
            departmentsHeaderTitle = "학과 공지 카테고리",
            onItemClick = {},
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}