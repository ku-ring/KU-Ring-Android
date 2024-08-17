package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.DepartmentWithAddIcon
import com.ku_stacks.ku_ring.designsystem.components.DepartmentWithCheckIcon
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.SearchTextField
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.onboarding.R
import com.ku_stacks.ku_ring.onboarding.compose.OnboardingViewModel
import com.ku_stacks.ku_ring.ui_util.preview_data.previewDepartments

@Composable
internal fun SetDepartmentScreen(
    viewModel: OnboardingViewModel,
    onSetDepartmentComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val isInitialSearch by viewModel.isInitialSearch.collectAsStateWithLifecycle()
    val departments by viewModel.departments.collectAsStateWithLifecycle()
    val selectedDepartment by viewModel.selectedDepartment.collectAsStateWithLifecycle()

    SetDepartmentScreen(
        query = query,
        onQueryUpdate = viewModel::onQueryUpdate,
        isInitialSearch = isInitialSearch,
        departments = departments,
        onSearch = viewModel::search,
        onSelectDepartment = viewModel::selectDepartment,
        selectedDepartment = selectedDepartment,
        onSetDepartmentComplete = onSetDepartmentComplete,
        modifier = modifier,
    )
}

@Composable
private fun SetDepartmentScreen(
    query: String?,
    onQueryUpdate: (String) -> Unit,
    isInitialSearch: Boolean,
    departments: List<Department>,
    onSearch: () -> Unit,
    onSelectDepartment: (Department) -> Unit,
    selectedDepartment: Department?,
    onSetDepartmentComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(KuringTheme.colors.background)) {
        SetDepartmentTitle(modifier = Modifier.padding(top = 76.dp, start = 20.dp))
        SetDepartmentSubtitle(modifier = Modifier.padding(top = 16.dp, start = 20.dp))
        SearchDepartmentTextField(
            query = query,
            onQueryUpdate = onQueryUpdate,
            onSearch = onSearch,
            modifier = Modifier
                .padding(start = 20.dp, top = 32.dp, end = 20.dp)
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchedDepartments(
            isVisible = !isInitialSearch,
            departments = departments,
            selectedDepartment = selectedDepartment,
            onSelectDepartment = onSelectDepartment,
            modifier = Modifier.weight(1f),
        )
        KuringCallToAction(
            text = stringResource(id = R.string.set_department_cta_text),
            onClick = {
                if (selectedDepartment != null) {
                    onSetDepartmentComplete()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            blur = true,
            enabled = (selectedDepartment != null),
        )
    }
}

@Composable
private fun SetDepartmentTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.set_department_screen_title),
        modifier = modifier,
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 34.08.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = KuringTheme.colors.textTitle,
        )
    )
}

@Composable
private fun SetDepartmentSubtitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.set_department_screen_subtitle),
        modifier = modifier,
        style = TextStyle(
            fontSize = 15.sp,
            lineHeight = 24.45.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
        )
    )
}

@Composable
private fun SearchDepartmentTextField(
    query: String?,
    onQueryUpdate: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchTextField(
        query = query ?: "",
        onQueryUpdate = onQueryUpdate,
        modifier = modifier,
        placeholderText = stringResource(id = R.string.set_department_text_field_placeholder),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
            onSearch()
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    )
}

@Composable
private fun SearchedDepartments(
    isVisible: Boolean,
    departments: List<Department>,
    selectedDepartment: Department?,
    onSelectDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        Column(modifier = modifier) {
            SearchedDepartmentsCaption(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
            )
            LazyColumn {
                items(departments) { department ->
                    SearchedDepartment(
                        department = department,
                        selectedDepartment = selectedDepartment,
                        onSelectDepartment = onSelectDepartment,
                    )
                }
            }
        }
    } else {
        Spacer(modifier = modifier)
    }
}

@Composable
private fun SearchedDepartment(
    department: Department,
    selectedDepartment: Department?,
    onSelectDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = selectedDepartment,
        modifier = modifier,
        label = "department item",
    ) {
        if (department == it) {
            DepartmentWithCheckIcon(
                department = department,
                onClickDepartment = {},
            )
        } else {
            DepartmentWithAddIcon(
                department = department,
                onAddDepartment = { onSelectDepartment(department) },
            )
        }
    }
}

@Composable
private fun SearchedDepartmentsCaption(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.set_department_search_result),
        style = TextStyle(
            fontSize = 15.sp,
            lineHeight = 24.45.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
        ),
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun SetDepartmentPreview() {
    var query by remember { mutableStateOf<String?>("학과") }
    var selectedDepartment by remember { mutableStateOf<Department?>(null) }

    KuringTheme {
        SetDepartmentScreen(
            query = query,
            onQueryUpdate = { query = it },
            isInitialSearch = false,
            onSearch = {},
            departments = previewDepartments,
            selectedDepartment = selectedDepartment,
            onSelectDepartment = { selectedDepartment = it },
            onSetDepartmentComplete = { },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}

