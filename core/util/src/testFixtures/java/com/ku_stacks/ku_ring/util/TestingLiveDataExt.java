package com.ku_stacks.ku_ring.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestingLiveDataExt {
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException, TimeoutException {
        return getOrAwaitValue(liveData, 2, TimeUnit.SECONDS);
    }

    public static <T> T getOrAwaitValue(final LiveData<T> liveData, long time, TimeUnit timeUnit)
            throws InterruptedException, TimeoutException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<>() {
            @Override
            public void onChanged(T value) {
                data[0] = value;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        try {
            // Wait for the value to be set
            if (!latch.await(time, timeUnit)) {
                throw new TimeoutException("LiveData value was never set.");
            }

            //noinspection unchecked
            return (T) data[0];
        } finally {
            liveData.removeObserver(observer);
        }
    }
}
