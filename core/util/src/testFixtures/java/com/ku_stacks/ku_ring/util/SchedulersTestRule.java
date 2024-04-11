package com.ku_stacks.ku_ring.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulersTestRule implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                // Create a new thread without waiting for the completion of the previous task for sequential execution
                RxJavaPlugins.setIoSchedulerHandler(schedulerSupplier -> Schedulers.trampoline());
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerSupplier -> Schedulers.trampoline());
                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}

