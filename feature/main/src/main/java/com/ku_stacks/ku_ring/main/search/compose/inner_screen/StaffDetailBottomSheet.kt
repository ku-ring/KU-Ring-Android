package com.ku_stacks.ku_ring.main.search.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
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
            color = MaterialTheme.colors.onSurface,
        )

        Text(
            text = staff.departmentAndCollege,
            style = TextStyle(
                fontFamily = Pretendard,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400)
            ),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_email),
                contentDescription = null,
                tint = Color.Gray,
            )

            Text(
                text = staff.email,
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400)
                ),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp, top = 9.dp, bottom = 9.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_pin),
                contentDescription = null,
                tint = Color.Gray,
            )

            Text(
                text = staff.lab,
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400)
                ),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp,  top = 9.dp, bottom = 9.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_phone),
                contentDescription = null,
                tint = Color.Gray,
            )

            Text(
                text = staff.phone,
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400)
                ),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp,  top = 9.dp, bottom = 9.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_major),
                contentDescription = null,
                tint = Color.Gray,
            )

            Text(
                text = staff.major,
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400)
                ),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp,  top = 9.dp, bottom = 9.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
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
            modifier = Modifier.background(MaterialTheme.colors.surface)
        )
    }
}
