package com.ku_stacks.ku_ring.edit_departments.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.*
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.edit_departments.EditDepartmentsViewModel
import com.ku_stacks.ku_ring.edit_departments.R
import com.ku_stacks.ku_ring.edit_departments.uimodel.DepartmentsUiModel

@Composable
internal fun EditDepartmentsScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditDepartmentsViewModel = hiltViewModel(),
) {
    val departmentsUiModel by viewModel.departments.collectAsStateWithLifecycle()

    EditDepartmentsScreen(
        onClose = onClose,
        onDeleteAllButtonClick = viewModel::onDeleteAllButtonClick,
        query = viewModel.query,
        onQueryUpdate = viewModel::onQueryUpdate,
        departmentsUiModel = departmentsUiModel,
        onAddDepartment = viewModel::onAddIconClick,
        onDeleteDepartment = viewModel::onDeleteIconClick,
        modifier = modifier,
    )

    viewModel.popupUiModel?.let { popupUiModel ->
        DepartmentPopup(
            popupUiModel = popupUiModel,
            onConfirm = viewModel::onPopupConfirmButtonClick,
            onDismiss = viewModel::onPopupDismissButtonClick,
        )
    }
}

@Composable
private fun EditDepartmentsScreen(
    onClose: () -> Unit,
    onDeleteAllButtonClick: () -> Unit,
    query: String,
    onQueryUpdate: (String) -> Unit,
    departmentsUiModel: DepartmentsUiModel,
    onAddDepartment: (Department) -> Unit,
    onDeleteDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDeleteAllButtonVisible = departmentsUiModel is DepartmentsUiModel.SelectedDepartments
    val isDeleteAllButtonEnabled =
        (departmentsUiModel as? DepartmentsUiModel.SelectedDepartments)?.departments?.isNotEmpty()
            ?: false
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        EditDepartmentsTitle(
            onClose = onClose,
            isDeleteAllButtonVisible = isDeleteAllButtonVisible,
            isDeleteAllButtonEnabled = isDeleteAllButtonEnabled,
            onDeleteAllButtonClick = onDeleteAllButtonClick,
            modifier = Modifier.fillMaxWidth(),
        )
        EditDepartmentsContents(
            query = query,
            onQueryUpdate = onQueryUpdate,
            departmentsUiModel = departmentsUiModel,
            onAddDepartment = onAddDepartment,
            onDeleteDepartment = onDeleteDepartment,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun EditDepartmentsTitle(
    onClose: () -> Unit,
    isDeleteAllButtonVisible: Boolean,
    isDeleteAllButtonEnabled: Boolean,
    onDeleteAllButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LargeTopAppBar(
        title = stringResource(id = R.string.edit_departments_app_bar_text),
        navigationIconId = R.drawable.ic_back_v2,
        onNavigationIconClick = onClose,
        modifier = modifier,
    ) {
        if (isDeleteAllButtonVisible) {
            DeleteAllDepartmentsButton(
                isDeleteAllButtonEnabled = isDeleteAllButtonEnabled,
                onDeleteAllButtonClick = onDeleteAllButtonClick,
            )
        }
    }
}

@Composable
private fun DeleteAllDepartmentsButton(
    isDeleteAllButtonEnabled: Boolean,
    onDeleteAllButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonAlpha by animateFloatAsState(
        targetValue = if (isDeleteAllButtonEnabled) 1f else 0.5f,
        label = "button alpha",
    )

    TextButton(
        onClick = onDeleteAllButtonClick,
        enabled = isDeleteAllButtonEnabled,
        modifier = modifier.alpha(buttonAlpha),
    ) {
        Text(
            text = stringResource(id = R.string.edit_departments_app_bar_delete_all),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.mainPrimary,
            )
        )
    }
}

@Composable
private fun EditDepartmentsContents(
    query: String,
    onQueryUpdate: (String) -> Unit,
    departmentsUiModel: DepartmentsUiModel,
    onAddDepartment: (Department) -> Unit,
    onDeleteDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        SearchTextField(
            query = query,
            onQueryUpdate = onQueryUpdate,
            placeholderText = stringResource(id = R.string.search_placeholder),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
        )
        Departments(
            departmentsUiModel = departmentsUiModel,
            onAddDepartment = onAddDepartment,
            onDeleteDepartment = onDeleteDepartment,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun Departments(
    departmentsUiModel: DepartmentsUiModel,
    onAddDepartment: (Department) -> Unit,
    onDeleteDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    val captionId = when (departmentsUiModel) {
        is DepartmentsUiModel.SelectedDepartments -> R.string.departments_caption_selected
        is DepartmentsUiModel.SearchedDepartments -> R.string.departments_caption_searched
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = captionId),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption1,
            ),
            modifier = Modifier.padding(start = 24.dp),
        )

        when (departmentsUiModel) {
            is DepartmentsUiModel.SelectedDepartments -> SelectedDepartments(
                uiModel = departmentsUiModel,
                onDeleteDepartment = onDeleteDepartment,
                modifier = Modifier.fillMaxWidth(),
            )

            is DepartmentsUiModel.SearchedDepartments -> SearchedDepartments(
                uiModel = departmentsUiModel,
                onAddDepartment = onAddDepartment,
                onDeleteDepartment = onDeleteDepartment,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SelectedDepartments(
    uiModel: DepartmentsUiModel.SelectedDepartments,
    onDeleteDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 6.dp),
    ) {
        items(uiModel.departments) {
            DepartmentWithDeleteIcon(
                department = it,
                onDeleteDepartment = onDeleteDepartment,
            )
        }
    }
}

@Composable
private fun SearchedDepartments(
    uiModel: DepartmentsUiModel.SearchedDepartments,
    onAddDepartment: (Department) -> Unit,
    onDeleteDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 6.dp),
    ) {
        items(uiModel.departments) {
            when {
                it.isSubscribed -> DepartmentWithCheckIcon(
                    department = it,
                    onClickDepartment = onDeleteDepartment,
                )

                else -> DepartmentWithAddIcon(
                    department = it,
                    onAddDepartment = onAddDepartment,
                )
            }
        }
    }
}

internal val previewDepartments = (1..5).map {
    Department(
        name = "smart ict  $it",
        shortName = "sicte$it",
        koreanName = "스마트ICT융합공학과 $it",
        isSubscribed = (it % 2 == 0),
        isSelected = (it == 2),
        isNotificationEnabled = (it % 2 == 1),
    )
}

@LightPreview
@Composable
private fun EditDepartmentsPreview_SelectedDepartments() {
    var query by remember { mutableStateOf("") }
    KuringTheme {
        EditDepartmentsScreen(
            onClose = {},
            onDeleteAllButtonClick = {},
            query = query,
            onQueryUpdate = { query = it },
            departmentsUiModel = DepartmentsUiModel.SelectedDepartments(previewDepartments),
            onAddDepartment = {},
            onDeleteDepartment = {},
            modifier = Modifier
                .fillMaxSize()
                .background(KuringTheme.colors.background),
        )
    }
}

@DarkPreview
@Composable
private fun EditDepartmentsPreview_SearchedDepartments() {
    var query by remember { mutableStateOf("") }
    KuringTheme {
        EditDepartmentsScreen(
            onClose = {},
            onDeleteAllButtonClick = {},
            query = query,
            onQueryUpdate = { query = it },
            departmentsUiModel = DepartmentsUiModel.SearchedDepartments(previewDepartments),
            onAddDepartment = {},
            onDeleteDepartment = {},
            modifier = Modifier
                .fillMaxSize()
                .background(KuringTheme.colors.background),
        )
    }
}