package com.ku_stacks.ku_ring.ui.edit_subscription.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ku_stacks.ku_ring.ui.edit_subscription.SubscriptionUiModel
import com.ku_stacks.ku_ring.ui.compose.theme.KuringTheme

@Composable
fun Subscriptions(
    categories: List<SubscriptionUiModel>,
    categoriesHeaderTitle: String,
    departments: List<SubscriptionUiModel>,
    departmentsHeaderTitle: String,
    onItemClick: (SubscriptionUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCategoryOpen by rememberSaveable { mutableStateOf(false) }
    var isDepartmentOpen by rememberSaveable { mutableStateOf(false) }

    ConstraintLayout(modifier = modifier) {
        val (categoryGrid, departmentGrid) = createRefs()
        SubscriptionDrawer(
            title = categoriesHeaderTitle,
            subscriptionItems = categories,
            onHeaderClick = { isCategoryOpen = !isCategoryOpen },
            onItemClick = onItemClick,
            isOpen = isCategoryOpen,
            columns = 3,
            modifier = Modifier.constrainAs(categoryGrid) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            }
        )
        SubscriptionDrawer(
            title = departmentsHeaderTitle,
            subscriptionItems = departments,
            onHeaderClick = { isDepartmentOpen = !isDepartmentOpen },
            onItemClick = onItemClick,
            isOpen = isDepartmentOpen,
            columns = 1,
            modifier = Modifier.constrainAs(departmentGrid) {
                top.linkTo(categoryGrid.bottom, margin = 26.dp)
                start.linkTo(categoryGrid.start)
                end.linkTo(categoryGrid.end)
                width = Dimension.matchParent
                height = Dimension.wrapContent
            }
        )
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