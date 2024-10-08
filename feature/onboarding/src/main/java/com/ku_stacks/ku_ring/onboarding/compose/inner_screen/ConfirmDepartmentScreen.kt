package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.onboarding.R
import com.ku_stacks.ku_ring.onboarding.compose.OnboardingViewModel
import com.ku_stacks.ku_ring.ui_util.preview_data.previewDepartments

@Composable
internal fun ConfirmDepartmentScreen(
    viewModel: OnboardingViewModel,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedDepartment by viewModel.selectedDepartment.collectAsStateWithLifecycle()

    ConfirmDepartmentScreen(
        selectedDepartment = selectedDepartment,
        onConfirm = {
            onConfirm()
            viewModel.subscribeSelectedDepartment()
        },
        onCancel = onCancel,
        modifier = modifier,
    )
}

@Composable
private fun ConfirmDepartmentScreen(
    selectedDepartment: Department?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(KuringTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(132.dp))
        ConfirmDepartmentTitles(selectedDepartment = selectedDepartment)
        Spacer(modifier = Modifier.weight(1f))
        ConfirmDepartmentActions(
            selectedDepartment = selectedDepartment,
            onConfirm = onConfirm,
            onCancel = onCancel,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ConfirmDepartmentActions(
    selectedDepartment: Department?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KuringCallToAction(
            onClick = {
                if (selectedDepartment != null) {
                    onConfirm()
                }
            },
            text = stringResource(id = R.string.confirm_department_confirm_text),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = stringResource(id = R.string.confirm_department_cancel_text),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 26.08.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption1,
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable(onClick = onCancel, role = Role.Button)
                .padding(8.dp),
        )
    }
}

@Composable
private fun ConfirmDepartmentTitles(
    selectedDepartment: Department?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SelectedDepartmentTitle(selectedDepartment = selectedDepartment)
        SelectedDepartmentCaption()
    }
}

@Composable
private fun SelectedDepartmentTitle(
    selectedDepartment: Department?,
    modifier: Modifier = Modifier,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = KuringTheme.colors.mainPrimary)) {
                append(selectedDepartment?.koreanName)
            }
            withStyle(SpanStyle(color = KuringTheme.colors.textTitle)) {
                append(stringResource(id = R.string.confirm_department_screen_title))
            }
        },
        modifier = modifier,
        fontSize = 24.sp,
        lineHeight = 34.08.sp,
        fontFamily = Pretendard,
        fontWeight = FontWeight(700),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun SelectedDepartmentCaption(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = (R.string.confirm_department_screen_caption)),
        style = TextStyle(
            fontSize = 15.sp,
            lineHeight = 24.45.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
            textAlign = TextAlign.Center,
        ),
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun ConfirmDepartmentPreview() {
    KuringTheme {
        ConfirmDepartmentScreen(
            selectedDepartment = previewDepartments[0],
            onConfirm = { },
            onCancel = { },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}