package com.ku_stacks.ku_ring.edit_subscription.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.LightPreview
import com.ku_stacks.ku_ring.designsystem.theme.CaptionGray2
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.edit_subscription.EditSubscriptionTab
import com.ku_stacks.ku_ring.edit_subscription.EditSubscriptionViewModel
import com.ku_stacks.ku_ring.edit_subscription.R
import com.ku_stacks.ku_ring.edit_subscription.compose.components.DepartmentSubscriptionItem
import com.ku_stacks.ku_ring.edit_subscription.compose.components.NormalSubscriptionItem
import com.ku_stacks.ku_ring.edit_subscription.uimodel.DepartmentSubscriptionUiModel
import com.ku_stacks.ku_ring.edit_subscription.uimodel.NormalSubscriptionUiModel
import kotlinx.coroutines.launch

@Composable
fun Subscriptions(
    viewModel: EditSubscriptionViewModel,
    onAddDepartmentButtonClick: () -> Unit,
    onSubscriptionComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    Subscriptions(
        selectedTab = uiState.selectedTab,
        categories = uiState.categories,
        departments = uiState.departments,
        onTabClick = viewModel::onTabClick,
        onCategoryClick = viewModel::onNormalSubscriptionItemClick,
        onDepartmentClick = viewModel::onDepartmentSubscriptionItemClick,
        onAddDepartmentButtonClick = onAddDepartmentButtonClick,
        onSubscriptionComplete = onSubscriptionComplete,
        modifier = modifier,
    )
}

@Composable
private fun Subscriptions(
    selectedTab: EditSubscriptionTab,
    categories: List<NormalSubscriptionUiModel>,
    departments: List<DepartmentSubscriptionUiModel>,
    onTabClick: (EditSubscriptionTab) -> Unit,
    onCategoryClick: (Int) -> Unit,
    onDepartmentClick: (String) -> Unit,
    onAddDepartmentButtonClick: () -> Unit,
    onSubscriptionComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(MaterialTheme.colors.surface)
    ) {
        CenterTitleTopBar(
            title = stringResource(id = R.string.app_bar_title),
            action = stringResource(id = R.string.app_bar_action),
            onActionClick = onSubscriptionComplete,
            actionClickLabel = stringResource(id = R.string.department_subscription_complete),
        )
        SubscriptionTitle(modifier = Modifier.padding(start = 32.dp, top = 30.dp))
        SubscriptionTabs(
            selectedTab = selectedTab,
            onTabClick = onTabClick,
            categories = categories,
            departments = departments,
            onCategoryClick = onCategoryClick,
            onDepartmentClick = onDepartmentClick,
            onAddDepartmentButtonClick = onAddDepartmentButtonClick,
            onSubscriptionComplete = onSubscriptionComplete,
            modifier = Modifier
                .padding(top = 68.dp)
                .weight(1f),
        )
    }
}

@Composable
private fun SubscriptionTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.title),
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 36.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SubscriptionTabs(
    selectedTab: EditSubscriptionTab,
    onTabClick: (EditSubscriptionTab) -> Unit,
    categories: List<NormalSubscriptionUiModel>,
    departments: List<DepartmentSubscriptionUiModel>,
    onCategoryClick: (Int) -> Unit,
    onDepartmentClick: (String) -> Unit,
    onAddDepartmentButtonClick: () -> Unit,
    onSubscriptionComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = selectedTab.ordinal,
    )

    val currentPage = pagerState.currentPage
    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = currentPage,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[currentPage])
                        .padding(horizontal = 29.dp)
                )
            }
        ) {
            EditSubscriptionTab.values().forEach { tab ->
                SubscriptionTab(
                    tab = tab,
                    isSelected = tab == selectedTab,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(it.ordinal)
                        }
                        onTabClick(it)
                    },
                )
            }
        }
        SubscriptionPager(
            categories = categories,
            onCategoryClick = onCategoryClick,
            departments = departments,
            onDepartmentClick = onDepartmentClick,
            onAddDepartmentButtonClick = onAddDepartmentButtonClick,
            modifier = Modifier.weight(1f),
            onSubscriptionComplete = onSubscriptionComplete,
            pagerState = pagerState,
        )
    }
}

@Composable
private fun SubscriptionTab(
    tab: EditSubscriptionTab,
    isSelected: Boolean,
    onClick: (EditSubscriptionTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Tab(
        selected = isSelected,
        onClick = { onClick(tab) },
        modifier = modifier,
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = Color(0xFF9A9A9A),
    ) {
        Text(
            text = stringResource(id = tab.tabTitleId),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(700),
            ),
            modifier = Modifier.padding(horizontal = 27.dp, vertical = 14.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SubscriptionPager(
    categories: List<NormalSubscriptionUiModel>,
    onCategoryClick: (Int) -> Unit,
    departments: List<DepartmentSubscriptionUiModel>,
    onDepartmentClick: (String) -> Unit,
    onAddDepartmentButtonClick: () -> Unit,
    onSubscriptionComplete: () -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
) {
    HorizontalPager(
        pageCount = EditSubscriptionTab.values().size,
        verticalAlignment = Alignment.Top,
        state = pagerState,
        modifier = modifier,
    ) { index ->
        when (index) {
            EditSubscriptionTab.NORMAL.ordinal -> {
                NormalCategoryPage(
                    categories = categories,
                    onCategoryClick = onCategoryClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            EditSubscriptionTab.DEPARTMENT.ordinal -> {
                DepartmentCategoryPage(
                    departments = departments,
                    onDepartmentClick = onDepartmentClick,
                    onAddDepartmentButtonClick = onAddDepartmentButtonClick,
                    onCallToActionClick = onSubscriptionComplete,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun NormalCategoryPage(
    categories: List<NormalSubscriptionUiModel>,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 46.dp, start = 31.dp, end = 31.dp, bottom = 10.dp),
        modifier = modifier,
    ) {
        itemsIndexed(
            items = categories,
            key = { _, category -> category.categoryName },
        ) { index, category ->
            NormalSubscriptionItem(
                uiModel = category,
                onClick = { onCategoryClick(index) },
                modifier = Modifier.aspectRatio(1f, matchHeightConstraintsFirst = true),
            )
        }
    }
}

@Composable
private fun DepartmentCategoryPage(
    departments: List<DepartmentSubscriptionUiModel>,
    onDepartmentClick: (String) -> Unit,
    onAddDepartmentButtonClick: () -> Unit,
    onCallToActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (departments.isEmpty()) {
        DepartmentCategoryEmptyIndicator(
            onAddDepartmentButtonClick = onAddDepartmentButtonClick,
            modifier = modifier,
        )
    } else {
        DepartmentCategoryList(
            departments = departments,
            onDepartmentClick = onDepartmentClick,
            onCallToActionClick = onCallToActionClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun DepartmentCategoryEmptyIndicator(
    onAddDepartmentButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        DepartmentEmptyIndicatorText(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        KuringCallToAction(
            text = stringResource(id = R.string.department_subscription_add_department),
            onClick = onAddDepartmentButtonClick,
            modifier = Modifier
                .padding(26.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun DepartmentEmptyIndicatorText(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.department_subscription_empty_message),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 22.5.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = CaptionGray2,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun DepartmentCategoryList(
    departments: List<DepartmentSubscriptionUiModel>,
    onDepartmentClick: (String) -> Unit,
    onCallToActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(
                items = departments,
                key = { it.name },
            ) { department ->
                DepartmentSubscriptionItem(
                    uiModel = department,
                    onClick = { onDepartmentClick(department.name) },
                )
            }
        }
        KuringCallToAction(
            text = stringResource(id = R.string.department_subscription_complete),
            onClick = onCallToActionClick,
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .padding(26.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SubscriptionsPreview() {
    val categories =
        NormalSubscriptionUiModel.initialValues.mapIndexed { index, normalSubscriptionUiModel ->
            normalSubscriptionUiModel.copy(isSelected = index < 3)
        }
    val departments = List(50) {
        DepartmentSubscriptionUiModel("쿠링학과$it", it % 2 == 0)
    }
    var selectedTab by remember { mutableStateOf(EditSubscriptionTab.NORMAL) }

    KuringTheme {
        Subscriptions(
            selectedTab = selectedTab,
            onTabClick = { selectedTab = EditSubscriptionTab.values()[1 - selectedTab.ordinal] },
            categories = categories,
            departments = departments,
            onCategoryClick = {},
            onDepartmentClick = {},
            onAddDepartmentButtonClick = {},
            onSubscriptionComplete = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@LightPreview
@Composable
private fun DepartmentPagePreview_Empty() {
    KuringTheme {
        Subscriptions(
            selectedTab = EditSubscriptionTab.DEPARTMENT,
            onTabClick = {},
            categories = emptyList(),
            departments = emptyList(),
            onCategoryClick = {},
            onDepartmentClick = {},
            onAddDepartmentButtonClick = {},
            onSubscriptionComplete = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}