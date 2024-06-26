package com.ku_stacks.ku_ring.util

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar

class DateUtilTest {
    @Test
    fun timeDiff_todayMidnight() {
        val current = Calendar.getInstance()
        val actual = DateUtil.getCalendar(0, 0, 0, 0)

        assertEquals(current.get(Calendar.DAY_OF_MONTH), actual?.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, actual?.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, actual?.get(Calendar.MINUTE))
        assertEquals(0, actual?.get(Calendar.SECOND))
    }

    @Test
    fun timeDiff_oneSecond() {
        val randomTime = DateUtil.getCalendar(0, 1, 10, 16)
        val after1Second = DateUtil.getCalendar(0, 1, 10, 17)

        assertNotNull(randomTime)
        assertNotNull(after1Second)
        val timeDiff = after1Second!!.timeInMillis - randomTime!!.timeInMillis
        assertEquals(1000L, timeDiff)
    }

    @Test
    fun timeDiff_oneHour() {
        val midnight = DateUtil.getCalendar(0, 0, 0, 0)
        val am1 = DateUtil.getCalendar(0, 0, 0, 0)?.apply {
            add(Calendar.HOUR_OF_DAY, 1)
        }

        assertNotNull(midnight)
        assertNotNull(am1)
        val timeDiff = am1!!.timeInMillis - midnight!!.timeInMillis
        assertEquals(60 * 60 * 1000L, timeDiff)
    }

    @Test
    fun timeDiff_illegalHourArgument() {
        assertNull(DateUtil.getCalendar(0, 27, 0, 1))
    }
}