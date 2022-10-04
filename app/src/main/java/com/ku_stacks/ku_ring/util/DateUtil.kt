package com.ku_stacks.ku_ring.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    
    @JvmStatic
    fun getToday(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        return simpleDateFormat.format(System.currentTimeMillis())
    }

    @JvmStatic
    fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.KOREA)
        return simpleDateFormat.format(System.currentTimeMillis())
    }

    @JvmStatic
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

    @JvmStatic
    fun convertDateToDay(str: String): String {
        val oldSimpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val date = oldSimpleDateFormat.parse(str)
        val newSimpleDateFormat = SimpleDateFormat("yyyy.MM.dd (E)", Locale.KOREA)
        return if (date == null) {
            str
        } else {
            newSimpleDateFormat.format(date)
        }
    }

    @JvmStatic
    fun convertLongToHHMM(timeMillis: Long): String {
        val simpleDateFormat = SimpleDateFormat("h:mm a", Locale.US)
        return simpleDateFormat.format(timeMillis)
    }

    @JvmStatic
    fun convertLongToDate(timeMillis: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)
        return simpleDateFormat.format(timeMillis)
    }

    @JvmStatic
    fun areSameDate(a: Long, b: Long): Boolean {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        return simpleDateFormat.format(a) == simpleDateFormat.format(b)
    }
}