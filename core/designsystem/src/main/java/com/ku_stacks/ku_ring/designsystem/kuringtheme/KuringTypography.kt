package com.ku_stacks.ku_ring.designsystem.kuringtheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.KuringTypographyTokens

/**
 * 쿠링 앱에서 사용하는 폰트 테마이다.
 * [androidx.compose.material.Typography]를 참고하여 개발했다.
 */
@Immutable
class KuringTypography(
    val noticeTitle: TextStyle = KuringTypographyTokens.NoticeTitle,
    val title1: TextStyle = KuringTypographyTokens.Title1,
    val title2: TextStyle = KuringTypographyTokens.Title2,
    val viewTitle: TextStyle = KuringTypographyTokens.ViewTitle,
    val titleSB: TextStyle = KuringTypographyTokens.TitleSB,
    val title2M: TextStyle = KuringTypographyTokens.Title2M,
    val body1: TextStyle = KuringTypographyTokens.Body1,
    val body2: TextStyle = KuringTypographyTokens.Body2,
    val body2R: TextStyle = KuringTypographyTokens.Body2R,
    val body2SB: TextStyle = KuringTypographyTokens.Body2SB,
    val caption1: TextStyle = KuringTypographyTokens.Caption1,
    val caption1_1: TextStyle = KuringTypographyTokens.Caption1_1,
    val caption2: TextStyle = KuringTypographyTokens.Caption2,
    val tag: TextStyle = KuringTypographyTokens.Tag,
    val tag2: TextStyle = KuringTypographyTokens.Tag2,
    val inputField: TextStyle = KuringTypographyTokens.InputField,
    val bottomNavigationDefault: TextStyle = KuringTypographyTokens.BottomNavigationDefault,
    val date: TextStyle = KuringTypographyTokens.Date,
) {
    fun copy(
        noticeTitle: TextStyle = this.noticeTitle,
        title1: TextStyle = this.title1,
        title2: TextStyle = this.title2,
        viewTitle: TextStyle = this.viewTitle,
        titleSB: TextStyle = this.titleSB,
        title2M: TextStyle = this.title2M,
        body1: TextStyle = this.body1,
        body2: TextStyle = this.body2,
        body2R: TextStyle = this.body2R,
        body2SB: TextStyle = this.body2SB,
        caption1: TextStyle = this.caption1,
        caption1_1: TextStyle = this.caption1_1,
        caption2: TextStyle = this.caption2,
        tag: TextStyle = this.tag,
        tag2: TextStyle = this.tag2,
        inputField: TextStyle = this.inputField,
        bottomNavigationDefault: TextStyle = this.bottomNavigationDefault,
        date: TextStyle = this.date,
    ): KuringTypography = KuringTypography(
        noticeTitle = noticeTitle,
        title1 = title1,
        title2 = title2,
        viewTitle = viewTitle,
        titleSB = titleSB,
        title2M = title2M,
        body1 = body1,
        body2 = body2,
        body2R = body2R,
        body2SB = body2SB,
        caption1 = caption1,
        caption1_1 = caption1_1,
        caption2 = caption2,
        tag = tag,
        tag2 = tag2,
        inputField = inputField,
        bottomNavigationDefault = bottomNavigationDefault,
        date = date,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KuringTypography) return false

        if (noticeTitle != other.noticeTitle) return false
        if (title1 != other.title1) return false
        if (title2 != other.title2) return false
        if (viewTitle != other.viewTitle) return false
        if (titleSB != other.titleSB) return false
        if (title2M != other.title2M) return false
        if (body1 != other.body1) return false
        if (body2 != other.body2) return false
        if (body2R != other.body2R) return false
        if (body2SB != other.body2SB) return false
        if (caption1 != other.caption1) return false
        if (caption1_1 != other.caption1_1) return false
        if (caption2 != other.caption2) return false
        if (tag != other.tag) return false
        if (tag2 != other.tag2) return false
        if (inputField != other.inputField) return false
        if (bottomNavigationDefault != other.bottomNavigationDefault) return false
        if (date != other.date) return false
        return true
    }

    override fun hashCode(): Int {
        var result = noticeTitle.hashCode()
        result = 31 * result + title1.hashCode()
        result = 31 * result + title2.hashCode()
        result = 31 * result + viewTitle.hashCode()
        result = 31 * result + titleSB.hashCode()
        result = 31 * result + title2M.hashCode()
        result = 31 * result + body1.hashCode()
        result = 31 * result + body2.hashCode()
        result = 31 * result + body2R.hashCode()
        result = 31 * result + body2SB.hashCode()
        result = 31 * result + caption1.hashCode()
        result = 31 * result + caption1_1.hashCode()
        result = 31 * result + caption2.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + tag2.hashCode()
        result = 31 * result + inputField.hashCode()
        result = 31 * result + bottomNavigationDefault.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

    override fun toString(): String {
        return "KuringTypography(noticeTitle=$noticeTitle, " +
                "title1=$title1, title2=$title2, viewTitle=$viewTitle, " +
                "titleSB=$titleSB, title2M=$title2M, " +
                "body1=$body1, body2=$body2, body2R=$body2R, body2SB=$body2SB, " +
                "caption1=$caption1, caption1_1=$caption1_1, caption2=$caption2, " +
                "tag=$tag, tag2=$tag2, inputField=$inputField, " +
                "bottomNavigationDefault=$bottomNavigationDefault" +
                "date=$date"
    }
}

internal val LocalKuringTypography = staticCompositionLocalOf { KuringTypography() }

@Preview(showBackground = true)
@Composable
private fun KuringTypographyPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "KuringTheme NoticeTitle",
            style = KuringTypography().noticeTitle,
        )
        Text(
            "KuringTheme Title1",
            style = KuringTypography().title1,
        )
        Text(
            "KuringTheme Title2",
            style = KuringTypography().title2,
        )
        Text(
            "KuringTheme ViewTitle",
            style = KuringTypography().viewTitle,
        )
        Text(
            "KuringTheme TitleSB",
            style = KuringTypography().titleSB,
        )
        Text(
            "KuringTheme Title2M",
            style = KuringTypography().title2M,
        )
        Text(
            "KuringTheme Body1",
            style = KuringTypography().body1,
        )
        Text(
            "KuringTheme Body2",
            style = KuringTypography().body2,
        )
        Text(
            "KuringTheme Body2R",
            style = KuringTypography().body2R,
        )
        Text(
            "KuringTheme Body2SB",
            style = KuringTypography().body2SB,
        )
        Text(
            "KuringTheme Caption1",
            style = KuringTypography().caption1,
        )
        Text(
            "KuringTheme Caption1_1",
            style = KuringTypography().caption1_1,
        )
        Text(
            "KuringTheme Caption2",
            style = KuringTypography().caption2,
        )
        Text(
            "KuringTheme Tag",
            style = KuringTypography().tag,
        )
        Text(
            "KuringTheme Tag2",
            style = KuringTypography().tag2,
        )
        Text(
            "KuringTheme InputField",
            style = KuringTypography().inputField,
        )
        Text(
            "KuringTheme BottomNavigationDefault",
            style = KuringTypography().bottomNavigationDefault,
        )
        Text(
            "KuringTheme Date",
            style = KuringTypography().date,
        )
    }
}