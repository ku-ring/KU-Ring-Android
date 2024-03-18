package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LazyPagingNoticeItemColumn
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.notice.DepartmentNoticeScreenState
import com.ku_stacks.ku_ring.main.notice.DepartmentNoticeViewModel
import com.ku_stacks.ku_ring.main.notice.compose.components.DepartmentHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DepartmentNoticeScreen(
    viewModel: DepartmentNoticeViewModel,
    onNoticeClick: (Notice) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedDepartments by viewModel.subscribedDepartments.collectAsState()
    val noticesFlow by viewModel.currentDepartmentNotice.collectAsState()
    val notices = noticesFlow?.collectAsLazyPagingItems()

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            notices?.refresh()
            isRefreshing = false
        },
        refreshThreshold = 75.dp,
    )

    val departmentNoticeScreenState by viewModel.departmentNoticeScreenState.collectAsState()

    when (departmentNoticeScreenState) {
        DepartmentNoticeScreenState.InitialLoading -> {
            Box(modifier = modifier) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        DepartmentNoticeScreenState.DepartmentsEmpty -> {
            DepartmentEmptyScreen(
                onNavigateToEditDepartment = onNavigateToEditDepartment,
                modifier = modifier,
            )
        }

        DepartmentNoticeScreenState.DepartmentsNotEmpty -> {
            DepartmentNoticeScreen(
                selectedDepartments = selectedDepartments,
                onSelectDepartment = viewModel::selectDepartment,
                onNavigateToEditDepartment = onNavigateToEditDepartment,
                notices = notices,
                onNoticeClick = onNoticeClick,
                isRefreshing = isRefreshing,
                refreshState = refreshState,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun DepartmentEmptyScreen(
    onNavigateToEditDepartment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.department_screen_add_department_caption),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.45.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption1,
                textAlign = TextAlign.Center,
            ),
        )
        KuringCallToAction(
            onClick = onNavigateToEditDepartment,
            text = stringResource(id = R.string.department_screen_add_department_button),
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DepartmentNoticeScreen(
    selectedDepartments: List<Department>,
    onSelectDepartment: (Department) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    notices: LazyPagingItems<Notice>?,
    onNoticeClick: (Notice) -> Unit,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 250)
    )

    ModalBottomSheetLayout(
        sheetContent = {
            DepartmentSelectorBottomSheet(
                departments = selectedDepartments,
                onSelect = {
                    onSelectDepartment(it)
                    scope.launch {
                        sheetState.hide()
                    }
                },
                onNavigateToEditDepartment = onNavigateToEditDepartment,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = modifier,
    ) {
        val selectedDepartment = selectedDepartments.firstOrNull { it.isSelected }
        DepartmentNoticeScreenContent(
            selectedDepartment = selectedDepartment,
            sheetState = sheetState,
            refreshState = refreshState,
            notices = notices,
            onNoticeClick = onNoticeClick,
            isRefreshing = isRefreshing,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DepartmentNoticeScreenContent(
    selectedDepartment: Department?,
    sheetState: ModalBottomSheetState,
    refreshState: PullRefreshState,
    notices: LazyPagingItems<Notice>?,
    onNoticeClick: (Notice) -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            DepartmentHeader(
                selectedDepartmentName = selectedDepartment?.koreanName ?: "",
                onClick = { scope.launch { sheetState.show() } },
            )
            LazyPagingNoticeItemColumn(
                notices = notices,
                onNoticeClick = onNoticeClick,
                modifier = Modifier.pullRefresh(refreshState),
                noticeFilter = { !it.isImportant },
            )
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}