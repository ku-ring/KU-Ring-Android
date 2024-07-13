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
            "department" -> "학과" // TODO: 나중에 학과 이름으로 보여줘야 함
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
            "학과" -> "department"
            else -> str
        }
    }

    @JvmStatic
    fun convertKoreanToShortEnglish(str: String): String {
        return when (str) {
            "학사" -> "bch"
            "장학" -> "sch"
            "취창업" -> "emp"
            "국제" -> "nat"
            "학생" -> "stu"
            "산학" -> "ind"
            "일반" -> "nor"
            "도서관" -> "lib"
            "학과" -> "dep"
            else -> str
        }
    }
}