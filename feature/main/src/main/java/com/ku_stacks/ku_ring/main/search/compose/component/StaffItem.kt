package com.ku_stacks.ku_ring.main.search.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.values.Pretendard
import com.ku_stacks.ku_ring.domain.Staff

@Composable
fun StaffItem(
    staff: Staff,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 12.dp, horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = staff.name,
            style = TextStyle(
                fontFamily = Pretendard,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500)
            ),
            color = KuringTheme.colors.textBody,
        )

        Text(
            text = staff.departmentAndCollege,
            style = TextStyle(
                fontFamily = Pretendard,
                fontSize = 14.sp,
                lineHeight = 22.82.sp,
                fontWeight = FontWeight(400)
            ),
            color = KuringTheme.colors.textCaption1,
            modifier = Modifier.padding(top = 3.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun StaffItemPreview() {
    KuringTheme {
        StaffItem(
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
