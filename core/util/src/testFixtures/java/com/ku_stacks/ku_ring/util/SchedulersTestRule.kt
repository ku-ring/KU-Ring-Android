package com.ku_stacks.ku_ring.util

//class SchedulersTestRule : TestRule {
//    override fun apply(base: Statement, description: Description): Statement {
//        return object : Statement() {
//            override fun evaluate() {
//                /** 새로운 스레드를 생성하지 않고, 이전 작업의 완료를 대기 -> 순차 진행 */
//                RxJavaPlugins.setIoSchedulerHandler {
//                    Schedulers.trampoline()
//                }
//                RxAndroidPlugins.setInitMainThreadSchedulerHandler {
//                    Schedulers.trampoline()
//                }
//                try {
//                    base.evaluate()
//                } finally {
//                    RxJavaPlugins.reset()
//                    RxAndroidPlugins.reset()
//                }
//            }
//        }
//    }
//}