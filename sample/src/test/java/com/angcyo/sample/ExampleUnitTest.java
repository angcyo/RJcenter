package com.angcyo.sample;

import com.rsen.net.RRetrofit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
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

        assertEquals(d, 4);
        assertTrue("mm", d == 4);
    }
}
