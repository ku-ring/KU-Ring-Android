package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ku_stacks.ku_ring.designsystem.theme.Gray300
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.MainPrimarySelected
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.theme.TextCaption1
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
    ) {
        Spacer(modifier = Modifier.weight(1f))
        AddIconButton(onClick = { onAddDepartment(department) })
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
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Crossfade(
            targetState = department.isSelected,
            label = "department check/uncheck",
        ) {
            if (it) {
                CheckIconButton(onClick = { onClickDepartment(department) })
            } else {
                UncheckIconButton(onClick = { onClickDepartment(department) })
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
    ) {
        Spacer(modifier = Modifier.weight(1f))
        CheckIconButton(onClick = { onClickDepartment(department) })
    }
}

@Composable
private fun SelectedDepartmentMark(
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .clip(shape)
            .background(MainPrimarySelected, shape)
            .border(0.5.dp, MaterialTheme.colors.primary, shape)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = stringResource(id = R.string.selected_department),
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 19.56.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colors.primary,
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
            color = TextCaption1,
            textAlign = TextAlign.End,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick, role = Role.Button)
            .minimumInteractiveComponentSize(),
    )
}

@Composable
private fun AddIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = contentDescription,
            tint = Gray300,
        )
    }
}

@Composable
private fun CheckIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_checked),
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.primary,
        )
    }
}

@Composable
private fun UncheckIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_unchecked),
            contentDescription = contentDescription,
            tint = Gray300,
        )
    }
}

@Composable
private fun BaseDepartment(
    department: Department,
    modifier: Modifier = Modifier,
    contents: @Composable RowScope.() -> Unit = {},
) {
    val backgroundColor = MaterialTheme.colors.surface
    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DepartmentTitle(
            departmentName = department.koreanName,
            textColor = contentColorFor(backgroundColor),
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