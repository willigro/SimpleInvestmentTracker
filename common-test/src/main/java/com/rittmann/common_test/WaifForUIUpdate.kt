package com.rittmann.common_test

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Assert

object WaifForUIUpdate {
    fun waitFor(ms: Int) {
        val signal = CountDownLatch(1)
        try {
            signal.await(ms.toLong(), TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            Assert.fail(e.message)
        }
    }
}