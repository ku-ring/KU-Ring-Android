package com.ku_stacks.ku_ring.kuringbot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.kuringbot.R

@Composable
internal fun KuringBotLoginPopup(
    onDismiss: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(KuringTheme.colors.gray200)
            .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.kuringbot_login_popup_title),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = KuringTheme.colors.textTitle,
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(17.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_x_v2),
                    contentDescription = stringResource(R.string.kuringbot_login_popup_close),
                )
            }
        }
        TextButton(
            onClick = onLogin,
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.textButtonColors(KuringTheme.colors.mainPrimary),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.kuringbot_login_popup_cta),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = KuringTheme.colors.textTitle,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 3.dp),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun KuringBotLoginPopupPreview() {
    KuringTheme {
        KuringBotLoginPopup(
            onDismiss = {},
            onLogin = {},
        )
    }
}