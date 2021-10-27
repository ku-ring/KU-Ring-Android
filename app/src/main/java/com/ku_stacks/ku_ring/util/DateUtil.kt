package com.ku_stacks.ku_ring.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun getToday(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        return simpleDateFormat.format(System.currentTimeMillis())
    }

    fun isToday(str: String): Boolean {
        val today = getToday()
        return when (str.length) {
            8 -> {
                today == str
            }
            19 -> {
                val dateString = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10)
                today == dateString
            }
            else -> {
                Timber.e("DateUtil.isToday() : String length is not normal")
                false
            }
        }
    }
}