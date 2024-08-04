package com.ku_stacks.ku_ring.designsystem.kuringtheme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.*

/**
 * 쿠링 앱에서 사용하는 색깔 테마이다.
 * [androidx.compose.material.Colors]을 참고하여 개발했다.
 */
@Stable
class KuringColors internal constructor(
    background: Color,
    borderline: Color,
    warning: Color,
    kuringLogoText: Color,
    textTitle: Color,
    textBody: Color,
    textCaption1: Color,
    textCaption2: Color,
    textCaption3: Color,
    gray100: Color,
    gray200: Color,
    gray300: Color,
    gray400: Color,
    gray600: Color,
    mainPrimary: Color,
    mainPrimarySelected: Color,
    isLight: Boolean,
) {
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var borderline by mutableStateOf(borderline, structuralEqualityPolicy())
        internal set
    var warning by mutableStateOf(warning, structuralEqualityPolicy())
        internal set
    var kuringLogoText by mutableStateOf(kuringLogoText, structuralEqualityPolicy())
        internal set
    var textTitle by mutableStateOf(textTitle, structuralEqualityPolicy())
        internal set
    var textBody by mutableStateOf(textBody, structuralEqualityPolicy())
        internal set
    var textCaption1 by mutableStateOf(textCaption1, structuralEqualityPolicy())
        internal set
    var textCaption2 by mutableStateOf(textCaption2, structuralEqualityPolicy())
        internal set
    var textCaption3 by mutableStateOf(textCaption3, structuralEqualityPolicy())
        internal set
    var gray100 by mutableStateOf(gray100, structuralEqualityPolicy())
        internal set
    var gray200 by mutableStateOf(gray200, structuralEqualityPolicy())
        internal set
    var gray300 by mutableStateOf(gray300, structuralEqualityPolicy())
        internal set
    var gray400 by mutableStateOf(gray400, structuralEqualityPolicy())
        internal set
    var gray600 by mutableStateOf(gray600, structuralEqualityPolicy())
        internal set
    var mainPrimary by mutableStateOf(mainPrimary, structuralEqualityPolicy())
        internal set
    var mainPrimarySelected by mutableStateOf(mainPrimarySelected, structuralEqualityPolicy())
        internal set
    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
        internal set

    fun copy(
        background: Color = this.background,
        borderline: Color = this.borderline,
        warning: Color = this.warning,
        kuringLogoText: Color = this.kuringLogoText,
        textTitle: Color = this.textTitle,
        textBody: Color = this.textBody,
        textCaption1: Color = this.textCaption1,
        textCaption2: Color = this.textCaption2,
        textCaption3: Color = this.textCaption3,
        gray100: Color = this.gray100,
        gray200: Color = this.gray200,
        gray300: Color = this.gray300,
        gray400: Color = this.gray400,
        gray600: Color = this.gray600,
        mainPrimary: Color = this.mainPrimary,
        mainPrimarySelected: Color = this.mainPrimarySelected,
        isLight: Boolean = this.isLight,
    ): KuringColors = KuringColors(
        background,
        borderline,
        warning,
        kuringLogoText,
        textTitle,
        textBody,
        textCaption1,
        textCaption2,
        textCaption3,
        gray100,
        gray200,
        gray300,
        gray400,
        gray600,
        mainPrimary,
        mainPrimarySelected,
        isLight,
    )

    override fun toString(): String {
        return "KuringColors(" +
                "background=$background, " +
                "borderline=$borderline, " +
                "warning=$warning, " +
                "kuringLogoText=$kuringLogoText, " +
                "textTitle=$textTitle, " +
                "textBody=$textBody, " +
                "textCaption1=$textCaption1, " +
                "textCaption2=$textCaption2, " +
                "textCaption3=$textCaption3, " +
                "gray100=$gray100, " +
                "gray200=$gray200, " +
                "gray300=$gray300, " +
                "gray400=$gray400, " +
                "gray600=$gray600, " +
                "mainPrimary=$mainPrimary, " +
                "mainPrimarySelected=$mainPrimarySelected, " +
                "isLight=$isLight, " +
                ")"
    }
}

fun kuringLightColors() = KuringColors(
    background = BackgroundLight,
    borderline = BorderlineLight,
    warning = WarningLight,
    kuringLogoText = KuringLogoTextLight,
    textTitle = TextTitleLight,
    textBody = TextBodyLight,
    textCaption1 = TextCaption1Light,
    textCaption2 = TextCaption2Light,
    textCaption3 = TextCaption3Light,
    gray100 = Gray100Light,
    gray200 = Gray200Light,
    gray300 = Gray300Light,
    gray400 = Gray400Light,
    gray600 = Gray600Light,
    mainPrimary = MainPrimaryLight,
    mainPrimarySelected = MainPrimarySelectedLight,
    isLight = true,
)

fun kuringDarkColors() = KuringColors(
    background = BackgroundDark,
    borderline = BorderlineDark,
    warning = WarningDark,
    kuringLogoText = KuringLogoTextDark,
    textTitle = TextTitleDark,
    textBody = TextBodyDark,
    textCaption1 = TextCaption1Dark,
    textCaption2 = TextCaption2Dark,
    textCaption3 = TextCaption3Dark,
    gray100 = Gray100Dark,
    gray200 = Gray200Dark,
    gray300 = Gray300Dark,
    gray400 = Gray400Dark,
    gray600 = Gray600Dark,
    mainPrimary = MainPrimaryDark,
    mainPrimarySelected = MainPrimarySelectedDark,
    isLight = false,
)

internal fun KuringColors.updateColorsFrom(other: KuringColors) {
    background = other.background
    borderline = other.borderline
    warning = other.warning
    kuringLogoText = other.kuringLogoText
    textTitle = other.textTitle
    textBody = other.textBody
    textCaption1 = other.textCaption1
    textCaption2 = other.textCaption2
    gray100 = other.gray100
    gray200 = other.gray200
    gray300 = other.gray300
    gray400 = other.gray400
    gray600 = other.gray600
    mainPrimary = other.mainPrimary
    mainPrimarySelected = other.mainPrimarySelected
}

val KuringColors.mainBackground: Color
    get() = if (isLight) mainPrimary else background

/**
 * [KuringColors]를 UI 트리에 전달하기 윟나 CompositionLocal이다.
 * [androidx.compose.material.LocalColors]를 참고하여 개발하였다.
 */
internal val LocalKuringColors = staticCompositionLocalOf { kuringLightColors() }