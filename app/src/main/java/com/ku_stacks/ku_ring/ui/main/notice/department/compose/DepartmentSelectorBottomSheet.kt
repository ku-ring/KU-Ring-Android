package com.ku_stacks.ku_ring.ui.main.notice.department.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.ui.compose.LightAndDarkPreview
import com.ku_stacks.ku_ring.ui.compose.theme.KuringTheme
import com.ku_stacks.ku_ring.ui.compose.theme.SfProDisplay

@Composable
fun DepartmentSelectorBottomSheet(
    departments: List<Department>,
    onSelect: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    ConstraintLayout(modifier = modifier) {
        val (titleText, departmentItems) = createRefs()
        Text(
            text = stringResource(id = R.string.department_selector_bottom_sheet_title),
            fontFamily = SfProDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = colorResource(id = R.color.kus_label),
            modifier = Modifier.constrainAs(titleText) {
                top.linkTo(parent.top, margin = 24.dp)
                start.linkTo(parent.start, margin = 24.dp)
            }
        )
        Column(
            modifier = Modifier
                .constrainAs(departmentItems) {
                    top.linkTo(titleText.bottom, margin = 25.dp)
                    bottom.linkTo(parent.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .verticalScroll(scrollState)
        ) {
            departments.forEach { department ->
                DepartmentItem(
                    department = department,
                    onSelect = onSelect,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                )
            }
        }
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
            color = colorResource(id = R.color.kus_label),
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
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colorResource(id = R.color.kus_green)),
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


@Preview(showBackground = true)
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
            modifier = Modifier.fillMaxWidth(),
        )
    }
}