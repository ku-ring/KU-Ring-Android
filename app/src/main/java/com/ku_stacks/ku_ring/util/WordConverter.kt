package com.ku_stacks.ku_ring.util

object WordConverter {

    @JvmStatic
    fun convertEnglishToKorean(str: String): String {
        return when (str) {
            "bachelor" -> "학사"
            "scholarship" -> "장학"
            "employment" -> "취창업"
            "national" -> "국제"
            "student" -> "학생"
            "industry_university" -> "산학"
            "normal" -> "일반"
            "library" -> "도서관"
            else -> str
        }
    }

    @JvmStatic
    fun convertKoreanToEnglish(str: String): String {
        return when (str) {
            "학사" -> "bachelor"
            "장학" -> "scholarship"
            "취창업" -> "employment"
            "국제" -> "national"
            "학생" -> "student"
            "산학" -> "industry_university"
            "일반" -> "normal"
            "도서관" -> "library"
            else -> str
        }
    }
}