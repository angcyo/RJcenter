package com.angcyo.sample;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.angcyo.sample.rx.RxDemo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RxDemo.log(RxDemo.getMethodName());
    }

    public void testRxDemo() throws Exception {
        RxDemo.createOperator();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        RxDemo.log(RxDemo.getMethodName());
    }
}
