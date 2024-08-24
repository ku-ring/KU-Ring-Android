package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Department

@Composable
fun DepartmentWithDeleteIcon(
    department: Department,
    onDeleteDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseDepartment(
        department = department,
        modifier = modifier,
    ) {
        if (department.isSelected) {
            SelectedDepartmentMark()
        }
        Spacer(modifier = Modifier.weight(1f))
        DeleteButton(onClick = { onDeleteDepartment(department) })
    }
}

@Composable
fun DepartmentWithAddIcon(
    department: Department,
    onAddDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseDepartment(
        department = department,
        modifier = modifier,
        onClick = { onAddDepartment(department) }
    ) {
        Spacer(modifier = Modifier.weight(1f))
        AddIcon()
    }
}

@Composable
fun DepartmentWithCheckOrUncheckIcon(
    department: Department,
    onClickDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseDepartment(
        department = department,
        modifier = modifier,
        onClick = { onClickDepartment(department) }
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Crossfade(
            targetState = department.isSelected,
            label = "department check/uncheck",
        ) {
            if (it) {
                CheckIcon()
            } else {
                UncheckIcon()
            }
        }
    }
}

@Composable
fun DepartmentWithCheckIcon(
    department: Department,
    onClickDepartment: (Department) -> Unit,
    modifier: Modifier = Modifier,
) {
    BaseDepartment(
        department = department,
        modifier = modifier,
        onClick = { onClickDepartment(department) },
        role = Role.Checkbox,
    ) {
        if (department.isSelected) {
            SelectedDepartmentMark()
        }
        Spacer(modifier = Modifier.weight(1f))
        CheckIcon()
    }
}

@Composable
private fun SelectedDepartmentMark(
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .background(KuringTheme.colors.mainPrimarySelected, shape)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = stringResource(id = R.string.selected_department),
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 19.56.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(600),
                color = KuringTheme.colors.mainPrimary,
            ),
        )
    }
}

@Composable
private fun DeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.notice_item_delete_button),
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
            textAlign = TextAlign.End,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick, role = Role.Button)
            .minimumInteractiveComponentSize(),
    )
}

@Composable
private fun AddIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_plus_v2),
        contentDescription = contentDescription,
        tint = KuringTheme.colors.gray300,
        modifier = modifier.minimumInteractiveComponentSize(),
    )
}

@Composable
private fun CheckIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_check_circle_fill_v2),
        contentDescription = contentDescription,
        tint = KuringTheme.colors.mainPrimary,
        modifier = modifier.minimumInteractiveComponentSize(),
    )
}

@Composable
private fun UncheckIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_check_circle_fill_2_v2),
        contentDescription = contentDescription,
        tint = KuringTheme.colors.gray300,
        modifier = modifier.minimumInteractiveComponentSize(),
    )
}

@Composable
private fun BaseDepartment(
    department: Department,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    role: Role? = if (onClick == null) null else Role.Button,
    contents: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() }, role = role)
            .background(KuringTheme.colors.background)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DepartmentTitle(
            departmentName = department.koreanName,
            textColor = KuringTheme.colors.textTitle,
        )
        contents()
    }
}

@Composable
private fun DepartmentTitle(
    departmentName: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
) {
    Text(
        text = departmentName,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = textColor,
        ),
        modifier = modifier,
    )
}

private val previewDepartment = Department(
    name = "smart_ict",
    shortName = "sicte",
    koreanName = "스마트ICT융합공학과",
    isSubscribed = true,
    isSelected = true,
    isNotificationEnabled = false,
)

@LightAndDarkPreview
@Composable
private fun DepartmentItemsPreview() {
    var isSelected by remember { mutableStateOf(false) }
    KuringTheme {
        Column(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth(),
        ) {
            DepartmentWithDeleteIcon(
                department = previewDepartment,
                onDeleteDepartment = {},
                modifier = Modifier.fillMaxWidth(),
            )
            DepartmentWithAddIcon(
                department = previewDepartment,
                onAddDepartment = {},
                modifier = Modifier.fillMaxWidth(),
            )
            DepartmentWithCheckOrUncheckIcon(
                department = previewDepartment.copy(isSelected = isSelected),
                onClickDepartment = { isSelected = !isSelected },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}