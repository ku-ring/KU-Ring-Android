package com.ku_stacks.ku_ring.ui.main.notice.department.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui_util.compose.LightAndDarkPreview
import com.ku_stacks.ku_ring.ui_util.compose.theme.KuringTheme
import com.ku_stacks.ku_ring.ui_util.compose.theme.SfProDisplay

@Composable
fun DepartmentHeader(
    selectedDepartmentName: String,
    onClick: () -> Unit,
    showArrow: Boolean,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
) {
    val description =
        if (showArrow) stringResource(id = R.string.department_selector_description) else ""

    Button(
        onClick = {
            if (isClickable) {
                onClick()
            }
        },
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .clearAndSetSemantics {
                contentDescription = description
            },
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.kus_tertiary_label)),
        elevation = null,
    ) {
        Text(
            text = selectedDepartmentName,
            fontFamily = SfProDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(id = R.color.kus_on_tertiary_label),
            modifier = Modifier.padding(vertical = 5.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showArrow) {
            Image(
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = null,
                modifier = Modifier
                    .rotate(90f)
                    .scale(0.8f),
                colorFilter = ColorFilter.tint(colorResource(R.color.kus_on_tertiary_label)),
            )
        }
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
            showArrow = showArrow,
            modifier = Modifier
                .padding(10.dp)
                .width(320.dp),
        )
    }
}