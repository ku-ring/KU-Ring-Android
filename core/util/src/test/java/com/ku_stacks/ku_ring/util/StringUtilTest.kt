package com.ku_stacks.ku_ring.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StringUtilTest {

    @Test
    fun `isOnlyAlphabets - empty string`() {
        isOnlyAlphabetsTest("", true)
    }

    @Test
    fun `isOnlyAlphabets - only alphabets`() {
        isOnlyAlphabetsTest("alphabet", true)
    }

    @Test
    fun `isOnlyAlphabets - only numbers`() {
        isOnlyAlphabetsTest("123456", false)
    }

    @Test
    fun `isOnlyAlphabets - alphabets and numbers`() {
        isOnlyAlphabetsTest("abc123", false)
    }

    @Test
    fun `isOnlyAlphabets - korean letters`() {
        isOnlyAlphabetsTest("가힣", false)
    }

    @Test
    fun `isOnlyAlphabets - special characters`() {
        isOnlyAlphabetsTest("???", false)
    }

    private fun isOnlyAlphabetsTest(string: String, expected: Boolean) {
        // given, when
        val actual = string.isOnlyAlphabets()
        // then
        assertThat(actual, `is`(expected))
    }


}