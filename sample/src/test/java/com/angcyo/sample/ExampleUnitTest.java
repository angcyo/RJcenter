package com.angcyo.sample;

import com.angcyo.sample.rx.RxDemo;
import com.rsen.net.RRetrofit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    public static void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void retrofitTest() {
        RRetrofit.getFactory();
    }

    @Test
    public void debugKeyTest() {
        int a = 1;
        int b = 2;
        int c = a + b;
        int d = c;
        int e = c;

        assertEquals(d, 4);
        assertTrue("mm", d == 4);
    }

    @Test
    public void rxTest() {
//        RxDemo.createOperator();
        RxDemo.flatOperator();
        sleep();
        System.out.println("-------------------------------end");
//        CountDownLatch
    }
}
