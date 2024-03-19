package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R

@Composable
fun DepartmentHeader(
    selectedDepartmentName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(id = R.string.department_selector_description)

    Row(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .clearAndSetSemantics {
                contentDescription = description
                role = Role.Button
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 21.5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = selectedDepartmentName,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 27.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(700),
                color = KuringTheme.colors.textBody,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.ic_chevron_2),
            contentDescription = null,
            tint = KuringTheme.colors.gray400,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DepartmentHeaderPreview() {
    var showArrow by remember { mutableStateOf(false) }
    KuringTheme {
        DepartmentHeader(
            selectedDepartmentName = "산업디자인학과",
            onClick = { showArrow = !showArrow },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}