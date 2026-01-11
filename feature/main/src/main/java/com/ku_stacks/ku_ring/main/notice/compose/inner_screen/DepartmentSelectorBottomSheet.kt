package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.ui.department.DepartmentWithCheckOrUncheckIcon
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.main.R

@Composable
fun DepartmentSelectorBottomSheet(
    departments: List<Department>,
    onSelect: (Department) -> Unit,
    onNavigateToEditDepartment: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.height(332.dp)) {
        Text(
            text = stringResource(id = R.string.department_selector_bottom_sheet_title),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 27.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(700),
                color = KuringTheme.colors.textBody,
            ),
            modifier = Modifier.padding(start = 24.dp, top = 22.dp, bottom = 20.dp),
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .weight(1f),
        ) {
            departments.forEach { department ->
                DepartmentWithCheckOrUncheckIcon(
                    department = department,
                    onClickDepartment = onSelect,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        KuringCallToAction(
            onClick = onNavigateToEditDepartment,
            text = stringResource(id = R.string.department_selector_bottom_sheet_cta),
            modifier = Modifier.fillMaxWidth(),
            blur = true,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DepartmentSelectorBottomSheetPreview() {
    val departments = (0..3).map {
        Department(
            name = "",
            shortName = "",
            koreanName = "컴퓨터공학과",
            isSubscribed = false,
            isSelected = it == 0,
            isNotificationEnabled = false,
        )
    }
    KuringTheme {
        DepartmentSelectorBottomSheet(
            departments = departments,
            onSelect = {},
            onNavigateToEditDepartment = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(KuringTheme.colors.background),
        )
    }
}