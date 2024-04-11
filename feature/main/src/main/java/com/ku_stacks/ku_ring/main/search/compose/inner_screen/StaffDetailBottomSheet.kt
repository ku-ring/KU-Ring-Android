package com.ku_stacks.ku_ring.main.search.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Staff
import com.ku_stacks.ku_ring.main.R

@Composable
fun StaffDetailBottomSheet(
    staff: Staff,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 20.dp)
    ) {
        Text(
            text = staff.name,
            style = TextStyle(
                fontFamily = Pretendard,
                fontSize = 20.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight(700)
            ),
            color = KuringTheme.colors.textBody,
        )

        Text(
            text = staff.departmentAndCollege,
            style = TextStyle(
                fontFamily = Pretendard,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400)
            ),
            color = KuringTheme.colors.textBody,
            modifier = Modifier.padding(top = 4.dp)
        )

        InformationRow(
            text = staff.email,
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_email_v2),
            modifier = Modifier.padding(top = 16.dp)
        )

        InformationRow(
            text = staff.lab,
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pin_v2),
        )

        InformationRow(
            text = staff.phone,
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_phone_v2),
        )

        InformationRow(
            text = staff.major,
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_major_v2),
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun InformationRow(
    text: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
        )

        Text(
            text = text,
            style = TextStyle(
                fontFamily = Pretendard,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400)
            ),
            color = KuringTheme.colors.textBody,
            modifier = Modifier.padding(start = 8.dp,  top = 9.dp, bottom = 9.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun StaffDetailBottomSheetPreview() {
    KuringTheme {
        StaffDetailBottomSheet(
            staff = Staff(
                name = "하늘다람쥐",
                major = "Computer Architecture and Parallel Computing",
                lab = "A 사무실",
                phone = "010-1111-2222",
                email = "kkk@gmail.com",
                department = "컴퓨터공학과",
                college = "공과대학",
            ),
            modifier = Modifier.background(KuringTheme.colors.background)
        )
    }
}
