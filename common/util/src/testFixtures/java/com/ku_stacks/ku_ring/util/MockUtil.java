package com.ku_stacks.ku_ring.util;

import org.mockito.Mockito;

public class MockUtil {

    public static <T> T mock(Class<T> type) {
        return Mockito.mock(type);
    }

    // inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

}
