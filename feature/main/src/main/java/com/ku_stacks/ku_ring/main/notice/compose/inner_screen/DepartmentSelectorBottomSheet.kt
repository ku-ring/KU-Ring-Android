package com.ku_stacks.ku_ring.main.notice.compose.inner_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ku_stacks.ku_ring.designsystem.components.DepartmentWithCheckOrUncheckIcon
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.SfProDisplay
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

@Composable
private fun DepartmentItem(
    department: Department,
    onSelect: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = if (department.isSelected) {
        stringResource(id = R.string.selected_department_description, department.koreanName)
    } else {
        department.koreanName
    }
    ConstraintLayout(
        modifier = Modifier
            .background(KuringTheme.colors.background)
            .clickable { onSelect(department) }
            .then(modifier)
            .clearAndSetSemantics {
                contentDescription = description
            }
    ) {
        val (nameText, selectedIcon) = createRefs()
        Text(
            text = department.koreanName,
            fontFamily = SfProDisplay,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = KuringTheme.colors.textBody,
            modifier = Modifier.constrainAs(nameText) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        AnimatedVisibility(
            visible = department.isSelected,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.constrainAs(selectedIcon) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_check_circle_fill_v2),
                contentDescription = null,
                colorFilter = ColorFilter.tint(KuringTheme.colors.mainPrimary),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun DepartmentItemPreview() {
    val department = Department(
        name = "",
        shortName = "",
        koreanName = "컴퓨터공학과",
        isSubscribed = false,
        isSelected = true,
        isNotificationEnabled = false,
    )
    KuringTheme {
        DepartmentItem(
            department = department,
            onSelect = {},
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
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