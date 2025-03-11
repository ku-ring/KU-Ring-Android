package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ku_stacks.ku_ring.auth.compose.component.button.RoundedCornerButton
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import feature.auth.R.string.dialog_heading_sign_in_wrong
import feature.auth.R.string.dialog_sub_heading_sign_in_wrong
import feature.auth.R.string.dialog_proceed_sign_in_wrong

@Composable
fun AuthDialog(
    onDismissRequest: () -> Unit,
) {
    val backgroundColor = KuringTheme.colors.background
    val shape = RoundedCornerShape(8.dp)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = shape
                )
                .padding(40.dp),
        ) {
            Text(
                text = stringResource(id = dialog_heading_sign_in_wrong),
                style = TextStyle(
                    color = KuringTheme.colors.textTitle,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Bold,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
                )
            )

            Text(
                text = stringResource(id = dialog_sub_heading_sign_in_wrong),
                style = TextStyle(
                    color = KuringTheme.colors.textCaption1,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
                )
            )

            RoundedCornerButton(
                text = stringResource(id = dialog_proceed_sign_in_wrong),
                onClick = onDismissRequest,
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 51.dp),
                modifier = Modifier.padding(top = 38.dp),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun AuthDialogPreview() {
    KuringTheme {
        AuthDialog(onDismissRequest = {})
    }
}