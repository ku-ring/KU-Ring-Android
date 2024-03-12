package com.ku_stacks.ku_ring.main.notice.department.compose

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ku_stacks.ku_ring.designsystem.components.LazyPagingNoticeItemColumn
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.notice.department.DepartmentNoticeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DepartmentNoticeScreen(
    viewModel: DepartmentNoticeViewModel,
    onNoticeClick: (Notice) -> Unit,
    onShowDepartmentSubscribeBottomSheet: () -> Unit,
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
        refreshThreshold = 100.dp,
    )
    DepartmentNoticeScreen(
        selectedDepartments = selectedDepartments,
        onSelectDepartment = viewModel::selectDepartment,
        notices = notices,
        onNoticeClick = onNoticeClick,
        onFabClick = onShowDepartmentSubscribeBottomSheet,
        isRefreshing = isRefreshing,
        refreshState = refreshState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DepartmentNoticeScreen(
    selectedDepartments: List<Department>,
    onSelectDepartment: (Department) -> Unit,
    notices: LazyPagingItems<Notice>?,
    onNoticeClick: (Notice) -> Unit,
    onFabClick: () -> Unit,
    isRefreshing: Boolean,
    refreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 250)
    )

    val fabDescription = stringResource(id = R.string.add_department_text)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
            )
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = modifier,
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onFabClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clearAndSetSemantics {
                            contentDescription = fabDescription
                        },
                    backgroundColor = colorResource(R.color.kus_green),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }
            },
        ) { contentPadding ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                val (selector, noticesList, refreshIndicator) = createRefs()
                val selectedDepartment = selectedDepartments.firstOrNull { it.isSelected }
                DepartmentHeader(
                    selectedDepartmentName = selectedDepartment?.koreanName ?: "",
                    onClick = { scope.launch { sheetState.show() } },
                    modifier = Modifier.constrainAs(selector) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )
                LazyPagingNoticeItemColumn(
                    notices = notices,
                    onNoticeClick = onNoticeClick,
                    modifier = Modifier
                        .constrainAs(noticesList) {
                            top.linkTo(selector.bottom, margin = 14.dp)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                            height = Dimension.fillToConstraints
                        }
                        .pullRefresh(refreshState),
                )
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = refreshState,
                    modifier = Modifier.constrainAs(refreshIndicator) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
    }
}