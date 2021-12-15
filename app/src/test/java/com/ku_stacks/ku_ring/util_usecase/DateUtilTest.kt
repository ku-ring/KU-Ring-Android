package com.ku_stacks.ku_ring.util_usecase

import com.ku_stacks.ku_ring.util.DateUtil
import org.junit.Test

class DateUtilTest {

    @Test
    fun getTodayTest() {
        print(DateUtil.getToday())
    }

    @Test
    fun convertDateToDayTest() {
        val date = "2021-12-14 17:11:18"
        print(DateUtil.convertDateToDay(date))
        //형식 잘못되면 파싱 오류 -> 2020.11.02 (월) 이렇게 뜸
    }
}