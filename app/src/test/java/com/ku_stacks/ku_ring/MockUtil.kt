package com.ku_stacks.ku_ring

import org.mockito.Mockito

object MockUtil {

    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

}