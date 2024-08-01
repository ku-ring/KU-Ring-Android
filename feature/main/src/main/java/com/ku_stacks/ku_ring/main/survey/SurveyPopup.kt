package com.ku_stacks.ku_ring.main.survey

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import java.time.LocalDate

/**
 * 2024년 8월 5일까지 진행하는 설문조사 홍보용 임시 팝업입니다.
 * 설문조사 종료 후 첫 번째 배포에서 제거될 예정입니다.
 *
 * author: mwy3055
 */


@Composable
internal fun SurveyPopup(
    modifier: Modifier = Modifier,
    viewModel: SurveyPopupViewModel = hiltViewModel(),
) {
    var isHiddenByUser by rememberSaveable { mutableStateOf(false) }
    val isSurveyComplete by viewModel.isSurveyComplete.collectAsStateWithLifecycle()
    val isSurveyFinished = LocalDate.now() > LocalDate.of(2024, 8, 5)

    if (!isHiddenByUser && !isSurveyComplete && !isSurveyFinished) {
        val context = LocalContext.current
        SurveyPopup(
            onClose = { isHiddenByUser = true },
            onNavigateToSurvey = {
                navigateToSurveyPage(context)
                isHiddenByUser = true
                viewModel.onSurveyComplete()
            },
            modifier = modifier,
        )
    }
}

private fun navigateToSurveyPage(context: Context) {
    val surveyPageIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://tally.so/r/npVg48"))
    context.startActivity(surveyPageIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurveyPopup(
    onClose: () -> Unit,
    onNavigateToSurvey: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(onDismissRequest = onClose, modifier = modifier) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .clip(RoundedCornerShape(15.dp)),
        ) {
            SurveyContents(onClose = onClose)
            SurveyCTAButton(
                onClick = onNavigateToSurvey,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SurveyContents(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.kuring_survey_2024),
            contentDescription = "쿠링 발전을 위해 설문조사에 참여해 주세요!",
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_x_v2),
            contentDescription = "닫기",
            tint = Color(0xFFF2F3F5),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .scale(0.5f)
                .padding(top = 16.dp, end = 16.dp)
                .clickable(role = Role.Button, onClick = onClose),
        )
    }
}

@Composable
private fun SurveyCTAButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .clickable(role = Role.Button, onClick = onClick)
            .fillMaxWidth(),
    ) {
        Text(
            text = "설문 참여하러 가기",
            color = KuringTheme.colors.mainPrimary,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SurveyPopupPreview() {
    KuringTheme {
        SurveyPopup(
            onClose = {},
            onNavigateToSurvey = {},
            modifier = Modifier,
        )
    }
}